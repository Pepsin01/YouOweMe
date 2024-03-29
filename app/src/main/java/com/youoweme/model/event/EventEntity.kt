package com.youoweme.model.event

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class Event(
    val title: String,
    internal var id: Long = -1
)

@Entity
data class EventEntity(
    @ColumnInfo val title: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity")
    fun getAll(): List<EventEntity>

    @Query("SELECT * FROM EventEntity WHERE id = :id")
    fun get(id: Long): EventEntity?

//    @Insert
//    fun insertAll(vararg eventEntity: EventEntity)

    @Insert
    fun insert(eventEntity: EventEntity): Long

    @Delete
    fun delete(eventEntity: EventEntity)

    @Update
    fun update(eventEntity: EventEntity)
}

class EventsRepository @Inject constructor(
    private val eventsDataSource: EventsDataSource
)
{
    suspend fun fetchEvent(eventId: Long): Event? {
        //TODO: inject dispatcher for better testing
        return withContext(Dispatchers.IO) {
            eventsDataSource.fetchEvent(eventId)
        }
    }

    suspend fun fetchEvents(): List<Event> {
        return withContext(Dispatchers.IO) {
            eventsDataSource.fetchEvents()
        }
    }

    suspend fun addEvent(event: Event): Long {
        return withContext(Dispatchers.IO) {
            eventsDataSource.addEvent(event)
        }
    }

    suspend fun deleteEvent(event: Event) {
        return withContext(Dispatchers.IO) {
            eventsDataSource.deleteEvent(event)
        }
    }

    suspend fun updateEvent(event: Event) {
        return withContext(Dispatchers.IO) {
            eventsDataSource.updateEvent(event)
        }
    }
}
