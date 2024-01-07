package com.youoweme.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class Event(
    val id: Int,
    //TODO: Data put here
)

class EventsDataSource {

    private val events: List<Event> = listOf(Event(0), Event(1), Event(2))

    fun fetchEvents(): Flow<List<Event>> {
        // TODO: get from db
        return  flow {
            // simulating live updates
            emit(events.subList(0, 1))
            emit(events)
        }
    }

    fun fetchEvent(eventId: Int): Event {
        if (eventId >= events.size || eventId < 0) {
            throw IllegalArgumentException()
        }

        return events[eventId]
    }
}

class EventsRepository(
    private val eventsDataSource: EventsDataSource
)
{
    fun fetchEvent(eventId: Int): Event {
        return eventsDataSource.fetchEvent(eventId)
    }

    val Events: Flow<List<Event>> =
        eventsDataSource.fetchEvents()
}
