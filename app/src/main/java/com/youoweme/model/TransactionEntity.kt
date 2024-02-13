package com.youoweme.model

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Insert
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class Transaction (
    val eventId: Long,
    val amount: Double,
    val payer: String,
    val payee: String,
    val description: String,
    val date: Date,
    val id: Long = 0
)

@Entity
data class TransactionEntity (
    @ColumnInfo val eventId: Long,
    @ColumnInfo val amount: Double,
    @ColumnInfo val payer: String,
    @ColumnInfo val payee: String,
    @ColumnInfo val description: String,
    @ColumnInfo val date: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Dao
interface TransactionDao {
    @Query("SELECT * FROM TransactionEntity WHERE eventId = :eventId")
    fun getAll(eventId: Long): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE id = :id")
    fun get(id: Long): TransactionEntity? //TODO: if emtpy, will really return null?

    @Insert
    fun insert(transactionEntity: TransactionEntity): Long

    @Delete
    fun delete(transactionEntity: TransactionEntity)
}

class TransactionsRepository @Inject constructor(
    private val transactionDataSource: TransactionDataSource
)
{
    suspend fun fetchTransaction(transactionId: Long): Transaction? {
        return withContext(Dispatchers.IO) {
            transactionDataSource.fetchTransaction(transactionId)
        }
    }

    suspend fun fetchTransactions(eventId: Long): List<Transaction> {
        return withContext(Dispatchers.IO) {
            transactionDataSource.fetchTransactions(eventId)
        }
    }

    suspend fun addTransaction(transaction: Transaction): Long {
        return withContext(Dispatchers.IO) {
            transactionDataSource.addTransaction(transaction)
        }
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        return withContext(Dispatchers.IO) {
            transactionDataSource.deleteTransaction(transaction)
        }
    }
}