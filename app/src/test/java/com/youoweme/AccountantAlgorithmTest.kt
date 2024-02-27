package com.youoweme

import com.youoweme.algorithms.Accountant
import com.youoweme.model.toFixed
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import org.junit.Test

import org.junit.Assert.*

class AccountantAlgorithmTest {
    @Test
    fun getDebtsTest_1() {
        // arrange
        val accountant: Accountant = Accountant(0)
        val persons = listOf<Person>(
            Person(0, "C", 3.toFixed(), 3),
            Person(0, "D", (-3).toFixed(), 4),
            Person(0, "E", (-4).toFixed(), 5),
            Person(0, "G", (-6).toFixed(), 7),
            Person(0, "F", (-5).toFixed(), 6),
            Person(0, "A", 8.toFixed(), 1),
            Person(0, "B", 7.toFixed(), 2),

        )

        // act
        val debts = accountant.getDebts(persons)

        // assert
        assertEquals(5, debts.size)
        assertEquals(Debt(0, 6.toFixed(), 7, 1), debts[0])
        assertEquals(Debt(0, 2.toFixed(), 6, 1), debts[1])
        assertEquals(Debt(0, 3.toFixed(), 6, 2), debts[2])
        assertEquals(Debt(0, 4.toFixed(), 5, 2), debts[3])
        assertEquals(Debt(0, 3.toFixed(), 4, 3), debts[4])
    }

    @Test
    fun getDebtsTest_2() {
        // arrange
        val accountant: Accountant = Accountant(0)
        val persons = listOf<Person>(
            Person(0, "C", 7.toFixed(), 3),
            Person(0, "D", 0.toFixed(), 4),
            Person(0, "A", (-11).toFixed(), 1),
            Person(0, "B", 4.toFixed(), 2),

            )

        // act
        val debts = accountant.getDebts(persons)

        // assert
        assertEquals(2, debts.size)
        assertEquals(Debt(0, 7.toFixed(), 1, 3), debts[0])
        assertEquals(Debt(0, 4.toFixed(), 1, 2), debts[1])
    }

    private fun assertEquals(expected: Debt, actual: Debt) {
        assertEquals(expected.amount.value, actual.amount.value)
        assertEquals(expected.creditorId, actual.creditorId)
        assertEquals(expected.debtorId, actual.debtorId)
    }
}