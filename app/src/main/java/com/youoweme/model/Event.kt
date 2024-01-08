package com.youoweme.model

import javax.inject.Inject

//TODO: enhance model

data class Event(
    val id: Int,
    val title: String
    //TODO: add more data
) {
//    companion object {
//        //TODO: set last id from db? Or completely better way of giving / having ids?
//        var lastId: Int = 0
//    }

    //val id: Int = lastId++;
}

class EventsRepository @Inject constructor(
    private val eventsDataSource: EventsDataSource
)
{
    fun fetchEvent(eventId: Int): Event? {
        return eventsDataSource.fetchEvent(eventId)
    }

    fun fetchEvents(): List<Event> {
        return eventsDataSource.fetchEvents()
    }
}
