package com.youoweme.algorithms

import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction

class Accountant(private val epsilon: Double = 0.01) {
    fun getDebts(persons: List<Person>, transactions: List<Transaction>): List<Debt> {
        val debts = mutableListOf<Debt>()

        updateBalances(persons, transactions)
        val balances = persons.toList().sortedByDescending { person -> person.balance }

        var i : Int = 0
        var j : Int = balances.size - 1

        while (i < j) {
            debts.add(transaction(balances[i], balances[j]))

            if (balances[i].balance == 0.0) {
                i += 1
            }

            if (balances[j].balance == 0.0) {
                j -= 1
            }
        }

        return debts;
    }

    private fun transaction(person1: Person, person2: Person): Debt {
        if (person1.balance + person2.balance < 0.0) {
            val debt = Debt(person1.balance, person2.id, person1.id)

            person2.balance += person1.balance
            person1.balance = 0.0

            return debt
        }

        val debt = Debt(-1 * person2.balance, person2.id, person1.id)

        person1.balance += person2.balance
        person2.balance = 0.0

        return debt
    }

    fun updateBalances(persons: List<Person>, transactions: List<Transaction>)
    {
        val personToBalance : MutableMap<Long, Double> = persons.associateBy({ it.id }, { 0.0 }).toMutableMap()

        for (transaction in transactions) {
            personToBalance[transaction.payerId] = personToBalance[transaction.payerId]!! + transaction.amount
            personToBalance[transaction.payeeId] = personToBalance[transaction.payeeId]!! - transaction.amount
        }

        for (person in persons)
        {
            person.balance = personToBalance[person.id]!!
        }
    }
}