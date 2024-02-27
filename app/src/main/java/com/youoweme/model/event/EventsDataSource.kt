package com.youoweme.model.event

import com.youoweme.model.YouOweMeDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsDataSource @Inject constructor(
    db: YouOweMeDatabase
) {

    private val eventDao: EventDao = db.eventDao()

    fun fetchEvents(): List<Event> {
        val events  = eventDao.getAll()
        return events.map { e -> Event(e.title, e.id) }
    }

    fun fetchEvent(eventId: Long): Event? {
        val e = eventDao.get(eventId) ?: return null
        return Event(e.title, e.id)
    }

    fun addEvent(event: Event): Long {
        val ee = EventEntity(event.title)
        return eventDao.insert(ee)
    }

    fun deleteEvent(event: Event) {
        val ee = EventEntity(event.title, event.id)
        eventDao.delete(ee)
    }

    fun updateEvent(event: Event) {
        val ee = EventEntity(event.title, event.id)
        eventDao.update(ee)
    }
}