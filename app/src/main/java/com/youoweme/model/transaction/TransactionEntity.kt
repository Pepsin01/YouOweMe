package com.youoweme.model.transaction

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Update
import com.youoweme.model.FixedPointDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Transaction (
    val eventId: Long,
    val amount: FixedPointDouble,
    val payerId: Long,
    val payeeId: Long,
    val description: String,
    val id: Long = 0
)

@Entity
data class TransactionEntity (
    @ColumnInfo val eventId: Long,
    @ColumnInfo val amount: Int,
    @ColumnInfo val payerId: Long,
    @ColumnInfo val payeeId: Long,
    @ColumnInfo val description: String,
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

    @Update
    fun update(transactionEntity: TransactionEntity)
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

    suspend fun updateTransaction(transaction: Transaction) {
        return withContext(Dispatchers.IO) {
            transactionDataSource.updateTransaction(transaction)
        }
    }
}