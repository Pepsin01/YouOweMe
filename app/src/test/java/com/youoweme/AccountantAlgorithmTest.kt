package com.youoweme

import com.youoweme.algorithms.Accountant
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction
import org.junit.Test

import org.junit.Assert.*

class AccountantAlgorithmTest {
    @Test
    fun getDebtsTest_Ints() {
        // arrange
        val accountant: Accountant = Accountant(0)
        val persons = listOf<Person>(
            Person(0, "A", 0.0, 1),
            Person(0, "B", 0.0, 2),
            Person(0, "C", 0.0, 3),
            Person(0, "D", 0.0, 4),
            Person(0, "E", 0.0, 5),
            Person(0, "F", 0.0, 6),
            Person(0, "G", 0.0, 7),

        )
        val transactions = mutableListOf<Transaction>();
        transactions.add(Transaction(0, 4.0, 3, 1, ""))
        transactions.add(Transaction(0, 2.0, 4, 3, ""))
        transactions.add(Transaction(0, 1.0, 1, 4, ""))
        transactions.add(Transaction(0, 4.0, 2, 1, ""))
        transactions.add(Transaction(0, 1.0, 1, 5, ""))
        transactions.add(Transaction(0, 3.0, 7, 2, ""))
        transactions.add(Transaction(0, 6.0, 5, 7, ""))
        transactions.add(Transaction(0, 1.0, 7, 6, ""))

        // act
        val debts = accountant.getDebts(persons, transactions)

        // assert
        assertEquals(5, debts.size)
        assertEquals(Debt(0, 5.0, 1, 5), debts[0])
        assertEquals(Debt(0, 1.0, 1, 3), debts[1])
        assertEquals(Debt(0, 1.0, 7, 3), debts[2])
        assertEquals(Debt(0, 1.0, 7, 2), debts[3])
        assertEquals(Debt(0, 1.0, 6, 4), debts[4])
    }

    @Test
    fun getDebtsTest_Doubles() {
        // arrange
        val accountant: Accountant = Accountant(0)
        val persons = listOf<Person>(
            Person(0, "A", 0.0, 1),
            Person(0, "B", 0.0, 2),
            Person(0, "C", 0.0, 3),
            Person(0, "D", 0.0, 4),
            Person(0, "E", 0.0, 5),
            Person(0, "F", 0.0, 6),
            Person(0, "G", 0.0, 7),

            )
        val transactions = mutableListOf<Transaction>();
        transactions.add(Transaction(0, 4.21111, 3, 1, ""))
        transactions.add(Transaction(0, 2.100000001, 4, 3, ""))
        transactions.add(Transaction(0, 1.25, 1, 4, ""))
        transactions.add(Transaction(0, 4.99, 2, 1, ""))
        transactions.add(Transaction(0, 1.99999999, 1, 5, ""))
        transactions.add(Transaction(0, 3.49, 7, 2, ""))
        transactions.add(Transaction(0, 6.89, 5, 7, ""))
        transactions.add(Transaction(0, 1.79, 7, 6, ""))

        // act
        val debts = accountant.getDebts(persons, transactions)

        // assert
        assertEquals(6, debts.size)
        assertEquals(Debt(0, 4.89, 1, 5), debts[0])
        assertEquals(Debt(0, 1.06, 1, 3), debts[1])
        assertEquals(Debt(0, 1.049, 6, 3), debts[2])
        assertEquals(Debt(0, 0.74, 6, 2), debts[3])
        assertEquals(Debt(0, 0.759, 7, 2), debts[4])
        assertEquals(Debt(0, 0.85, 7, 4), debts[5])
    }

    private fun assertEquals(expected: Debt, actual: Debt) {
        assertEquals(expected.amount, actual.amount, 0.1)
        assertEquals(expected.creditorId, actual.creditorId)
        assertEquals(expected.debtorId, actual.debtorId)
    }
}