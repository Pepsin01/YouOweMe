package com.youoweme.model.transaction

import com.youoweme.model.YouOweMeDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionDataSource @Inject constructor(
    db: YouOweMeDatabase
) {

    private val transactionDao: TransactionDao = db.transactionDao()

    fun fetchTransactions(eventId: Long): List<Transaction> {
        val transactions  = transactionDao.getAll(eventId)
        return transactions.map { t -> Transaction(t.eventId, t.amount, t.payerId, t.payeeId, t.description, t.id) }
    }

    fun fetchTransaction(transactionId: Long): Transaction? {
        val t = transactionDao.get(transactionId) ?: return null
        return Transaction(t.eventId, t.amount, t.payerId, t.payeeId, t.description, t.id)
    }

    fun addTransaction(transaction: Transaction): Long {
        val t = TransactionEntity(transaction.eventId, transaction.amount, transaction.payerId, transaction.payeeId, transaction.description)
        return transactionDao.insert(t)
    }

    fun deleteTransaction(transaction: Transaction) {
        val t = transactionDao.get(transaction.id) ?: return
        transactionDao.delete(t)
    }

    fun updateTransaction(transaction: Transaction) {
        val t = TransactionEntity(transaction.eventId, transaction.amount, transaction.payerId, transaction.payeeId, transaction.description, transaction.id)
        transactionDao.update(t)
    }
}