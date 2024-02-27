package com.youoweme.model.person

import com.youoweme.model.YouOweMeDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonDataSource @Inject constructor(
    db: YouOweMeDatabase
) {

        private val personDao: PersonDao = db.personDao()

        fun fetchPersons(eventId : Long): List<Person> {
            val persons  = personDao.getAll(eventId)
            return persons.map { p -> Person(p.eventId, p.name, id = p.id) }
        }

        fun fetchPerson(personId: Long): Person? {
            val p = personDao.get(personId) ?: return null
            return Person(p.eventId, p.name, id = p.id)
        }

        fun addPerson(person: Person): Long {
            val p = PersonEntity(person.eventId, person.name)
            return personDao.insert(p)
        }

        fun deletePerson(personId: Long) {
            val e = personDao.get(personId) ?: return
            personDao.delete(e)
        }

        fun updatePerson(person: Person) {
            val p = PersonEntity(person.eventId, person.name, person.id)
            personDao.update(p)
        }
}