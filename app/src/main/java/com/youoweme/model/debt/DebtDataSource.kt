package com.youoweme.model.debt

import com.youoweme.model.toFixed
import com.youoweme.model.YouOweMeDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtDataSource @Inject constructor(
    db: YouOweMeDatabase
) {

    private val debtDao: DebtDao = db.debtDao()

    fun fetchDebts(eventId: Long): List<Debt> {
        val debts  = debtDao.getAll(eventId)
        return debts.map { d -> Debt(d.eventId, d.amount.toFixed(), d.debtorId, d.creditorId, d.id) }
    }

    fun fetchDebt(debtId: Long): Debt? {
        val d = debtDao.get(debtId) ?: return null
        return Debt(d.eventId, d.amount.toFixed(), d.debtorId, d.creditorId, d.id)
    }

    fun addDebt(debt: Debt): Long {
        val d = DebtEntity(debt.amount.value, debt.debtorId, debt.creditorId, debt.eventId)
        return debtDao.insert(d)
    }

    fun deleteDebt(debt: Debt) {
        val d = debtDao.get(debt.id) ?: return
        debtDao.delete(d)
    }
}