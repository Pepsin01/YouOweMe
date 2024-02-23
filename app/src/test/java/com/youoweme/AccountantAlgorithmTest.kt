package com.youoweme

import com.youoweme.algorithms.Accountant
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class AccountantAlgorithmTest {
    @Test
    fun getDebtsTest_1() {
        // arrange
        val accountant: Accountant = Accountant(0)
        val persons = listOf<Person>(
            Person(0, "C", 3.0, 3),
            Person(0, "D", -3.0, 4),
            Person(0, "E", -4.0, 5),
            Person(0, "G", -6.0, 7),
            Person(0, "F", -5.0, 6),
            Person(0, "A", 8.0, 1),
            Person(0, "B", 7.0, 2),

        )
        val transactions = listOf<Transaction>()

        // act
        val debts = accountant.getDebts(persons, transactions)

        // assert
        assertEquals(5, debts.size)
        AssertEqual(Debt(0, 6.0, 7, 1), debts[0])
        AssertEqual(Debt(0, 2.0, 6, 1), debts[1])
        AssertEqual(Debt(0, 3.0, 6, 2), debts[2])
        AssertEqual(Debt(0, 4.0, 5, 2), debts[3])
        AssertEqual(Debt(0, 3.0, 4, 3), debts[4])
    }

    private fun AssertEqual(expected: Debt, actual: Debt) {
        assertEquals(expected.amount, actual.amount, 0.01)
        assertEquals(expected.creditorId, actual.creditorId)
        assertEquals(expected.debtorId, actual.debtorId)
    }
}