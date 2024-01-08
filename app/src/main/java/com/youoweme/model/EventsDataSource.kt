package com.youoweme.model

import androidx.room.PrimaryKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsDataSource @Inject constructor(
    private val db: YouOweMeDatabase
) {

    private val eventDao: EventDao = db.eventDao()

    fun fetchEvents(): List<Event> {
        val events  = eventDao.getAll();
        return events.map { e -> Event(e.title, e.id) }
    }

    fun fetchEvent(eventId: Int): Event? {
        val e = eventDao.get(eventId) ?: return null;
        return Event(e.title, e.id)
    }

    fun addEvent(event: Event): Long {
        val ee = EventEntity(event.title)
        return eventDao.insert(ee)
    }
}