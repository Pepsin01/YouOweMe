package com.youoweme.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Insert
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class Person(val name: String, val id: Long)

@Entity
data class PersonEntity(
    @ColumnInfo val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
interface PersonDao {
    @Query("SELECT * FROM PersonEntity")
    fun getAll(): List<PersonEntity>

    @Query("SELECT * FROM PersonEntity WHERE id = :id")
    fun get(id: Int): PersonEntity?

    @Insert
    fun insert(personEntity: PersonEntity): Long

    @Delete
    fun delete(personEntity: PersonEntity)
}

class PersonsRepository @Inject constructor(
    private val personsDataSource: PersonDataSource
)
{
    suspend fun fetchPerson(personId: Int): Person? {
        return withContext(Dispatchers.IO) {
            personsDataSource.fetchPerson(personId)
        }
    }

    suspend fun fetchPersons(): List<Person> {
        return withContext(Dispatchers.IO) {
            personsDataSource.fetchPersons()
        }
    }

    suspend fun addPerson(person: Person): Long {
        return withContext(Dispatchers.IO) {
            personsDataSource.addPerson(person)
        }
    }

    suspend fun deletePerson(personId: Int) {
        return withContext(Dispatchers.IO) {
            personsDataSource.deletePerson(personId)
        }
    }
}