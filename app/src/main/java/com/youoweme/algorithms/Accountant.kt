package com.youoweme.algorithms

import com.youoweme.model.toFixed
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person

//TODO: nechci tu posilat eventId, k cemu?
class Accountant(private val eventId: Long) {
    fun getDebts(persons: List<Person>): List<Debt> {
        val debts = mutableListOf<Debt>()

        val balances = persons.toMutableList().map { person -> Person(person.eventId, person.name, person.balance, person.id) }.sortedByDescending { person -> person.balance }

        var i : Int = 0
        var j : Int = balances.size - 1

        while (i < j) {
            debts.add(transaction(balances[i], balances[j]))

            if (balances[i].balance == 0.toFixed()) {
                i += 1
            }

            if (balances[j].balance == 0.toFixed()) {
                j -= 1
            }
        }

        return debts
    }

    private fun transaction(person1: Person, person2: Person): Debt {
        if (person1.balance + person2.balance < 0.toFixed()) {
            val debt: Debt = Debt(eventId /* ugly :( */, person1.balance, person2.id, person1.id)

            person2.balance += person1.balance
            person1.balance = 0.toFixed()

            return debt
        }

        val debt = Debt(eventId /* ugly :( */, (-1).toFixed() * person2.balance, person2.id, person1.id)

        person1.balance += person2.balance
        person2.balance = 0.toFixed()

        return debt
    }
}