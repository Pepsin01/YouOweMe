package com.youoweme.algorithms

import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction

//TODO: nechci tu posilat eventId, k cemu?
class Accountant(private val eventId: Long) {
    fun getDebts(persons: List<Person>, transactions: List<Transaction>): List<Debt> {
        val debts = mutableListOf<Debt>()

        val balances = persons.toMutableList().map { person -> Person(person.eventId, person.name, person.balance, person.id) }.sortedByDescending { person -> person.balance }

        var i : Int = 0
        var j : Int = balances.size - 1

        while (i <= j) {
            debts.add(transaction(balances[i], balances[j], eventId))

            if (balances[i].balance == 0.0) {
                i += 1
            }

            if (balances[j].balance == 0.0) {
                j -= 1
            }
        }

        return debts;
    }

    private fun transaction(person1: Person, person2: Person, eventId: Long /* ugly */): Debt {
        if (person1.balance + person2.balance < 0) {
            val debt: Debt = Debt(eventId /* ugly :( */, person1.balance, person2.id, person1.id)

            person2.balance += person1.balance
            person1.balance = 0.0

            return debt
        }

        val debt = Debt(eventId /* ugly :( */, -1 * person2.balance, person2.id, person1.id)

        person1.balance += person2.balance
        person2.balance = 0.0

        return debt
    }
}