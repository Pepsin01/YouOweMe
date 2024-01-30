package com.youoweme.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonDataSource @Inject constructor(
    db: YouOweMeDatabase
) {

        private val personDao: PersonDao = db.personDao()

        fun fetchPersons(): List<Person> {
            val persons  = personDao.getAll()
            return persons.map { e -> Person(e.name, e.id) }
        }

        fun fetchPerson(personId: Int): Person? {
            val e = personDao.get(personId) ?: return null
            return Person(e.name, e.id)
        }

        fun addPerson(person: Person): Long {
            val ee = PersonEntity(person.name)
            return personDao.insert(ee)
        }

        fun deletePerson(personId: Int) {
            val e = personDao.get(personId) ?: return
            personDao.delete(e)
        }
}