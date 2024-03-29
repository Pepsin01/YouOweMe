package com.youoweme.model.person

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

data class Person(
    var eventId: Long,
    var name: String,
    var balance: Double = 0.0,
    var id: Long = 0
)

@Entity
data class PersonEntity(
    @ColumnInfo val eventId: Long,
    @ColumnInfo val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
interface PersonDao {
    @Query("SELECT * FROM PersonEntity WHERE eventId = :eventId")
    fun getAll(eventId: Long): List<PersonEntity>

    @Query("SELECT * FROM PersonEntity WHERE id = :id")
    fun get(id: Long): PersonEntity?

    @Insert
    fun insert(personEntity: PersonEntity): Long

    @Delete
    fun delete(personEntity: PersonEntity)

    @Update
    fun update(personEntity: PersonEntity)
}

class PersonsRepository @Inject constructor(
    private val personsDataSource: PersonDataSource
)
{
    suspend fun fetchPerson(personId: Long): Person? {
        return withContext(Dispatchers.IO) {
            personsDataSource.fetchPerson(personId)
        }
    }

    suspend fun fetchPersons(eventId: Long): List<Person> {
        return withContext(Dispatchers.IO) {
            personsDataSource.fetchPersons(eventId)
        }
    }

    suspend fun addPerson(person: Person): Long {
        return withContext(Dispatchers.IO) {
            personsDataSource.addPerson(person)
        }
    }

    suspend fun deletePerson(personId: Long) {
        return withContext(Dispatchers.IO) {
            personsDataSource.deletePerson(personId)
        }
    }

    suspend fun updatePerson(person: Person) {
        return withContext(Dispatchers.IO) {
            personsDataSource.updatePerson(person)
        }
    }
}