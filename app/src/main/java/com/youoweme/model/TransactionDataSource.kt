package com.youoweme.model

import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import java.text.DateFormat

@Singleton
class TransactionDataSource @Inject constructor(
    private val db: YouOweMeDatabase
) {

    private val transactionDao: TransactionDao = db.transactionDao()

    fun fetchTransactions(eventId: Long): List<Transaction> {
        val transactions  = transactionDao.getAll(eventId)
        return transactions.map { t -> Transaction(t.eventId, t.amount, t.payer, t.payee, t.description, DateFormat.getDateInstance().parse(t.date), t.id) }
    }

    fun fetchTransaction(transactionId: Long): Transaction? {
        val t = transactionDao.get(transactionId) ?: return null
        return Transaction(t.eventId, t.amount, t.payer, t.payee, t.description, DateFormat.getDateInstance().parse(t.date), t.id)
    }

    fun addTransaction(transaction: Transaction): Long {
        val t = TransactionEntity(transaction.eventId, transaction.amount, transaction.payer, transaction.payee, transaction.description, transaction.date.toString())
        return transactionDao.insert(t)
    }

    fun deleteTransaction(transaction: Transaction) {
        val t = transactionDao.get(transaction.id) ?: return
        transactionDao.delete(t)
    }
}