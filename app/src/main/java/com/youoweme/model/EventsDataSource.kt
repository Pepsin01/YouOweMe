package com.youoweme.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsDataSource @Inject constructor() {

    private val events: List<Event> = listOf(
        Event(0, "title1"),
        Event(1, "title2"),
        Event(2, "title3"),
        Event(3, "title4"),
        Event(4, "title5"),
        Event(5, "title6"),
        Event(6, "title7"),
        Event(7, "title8"),
    )

    fun fetchEvents(): List<Event> {
        return events;
    }

    fun fetchEvent(eventId: Int): Event? {
        return events.firstOrNull { event -> event.id == eventId }
    }
}