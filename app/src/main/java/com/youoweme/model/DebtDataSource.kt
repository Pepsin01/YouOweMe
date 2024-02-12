package com.youoweme.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtDataSource @Inject constructor(
    private val db: YouOweMeDatabase
) {

    private val debtDao: DebtDao = db.debtDao()

    fun fetchDebts(eventId: Long): List<Debt> {
        val debts  = debtDao.getAll(eventId)
        return debts.map { d -> Debt(d.eventId, d.amount, d.debtor, d.creditor, d.id) }
    }

    fun fetchDebt(debtId: Long): Debt? {
        val d = debtDao.get(debtId) ?: return null
        return Debt(d.eventId, d.amount, d.debtor, d.creditor, d.id)
    }

    fun addDebt(debt: Debt): Long {
        val d = DebtEntity(debt.amount, debt.debtor, debt.creditor, debt.eventId)
        return debtDao.insert(d)
    }

    fun deleteDebt(debt: Debt) {
        val d = debtDao.get(debt.id) ?: return
        debtDao.delete(d)
    }
}