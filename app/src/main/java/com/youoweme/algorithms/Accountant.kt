package com.youoweme.algorithms

import com.youoweme.model.Debt
import com.youoweme.model.Transaction

class Accountant(private val eventId: Long) {
    //Dumb algorithm that recalculates debts based on transactions
    //TODO: make this more efficient
    fun RecalculateDebts(currentDebts: List<Debt>, currnetTransactions: List<Transaction>): List<Debt> {
        val newDebts = mutableListOf<Debt>()
        for (transaction in currnetTransactions) {
            var found = false
            for (debt in newDebts) {
                if (debt.creditor == transaction.payer && debt.debtor == transaction.payee) {
                    debt.amount += transaction.amount
                    found = true
                    break
                }
            }
            if (!found) {
                newDebts += Debt(eventId, transaction.amount, transaction.payee, transaction.payer)
            }
        }
        return newDebts
    }
}