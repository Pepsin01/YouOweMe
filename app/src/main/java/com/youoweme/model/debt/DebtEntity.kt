package com.youoweme.model.debt

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.youoweme.model.FixedPointDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


//TODO: proc by mel byt model read only? Zmenil bych i u ostatnich ...
class Debt (
    var eventId: Long, //TODO: Fakt chceme eventID v modelu, osobne bych eventID vyplnit az ve fazi ukladani do databaze na Dao?? blbe se s tim pak pracuje, viz AccountantAlgoritmus
    var amount: FixedPointDouble,
    var debtorId: Long,
    var creditorId: Long,
    var id: Long = 0
)

@Entity
data class DebtEntity (
    @ColumnInfo val amount: Int,
    @ColumnInfo val debtorId: Long,
    @ColumnInfo val creditorId: Long,
    @ColumnInfo val eventId: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

@Dao
interface DebtDao {
    @androidx.room.Query("SELECT * FROM DebtEntity WHERE eventId = :eventId")
    fun getAll(eventId: Long): List<DebtEntity>

    @androidx.room.Query("SELECT * FROM DebtEntity WHERE id = :id")
    fun get(id: Long): DebtEntity? //TODO: if emtpy, will really return null?

    @androidx.room.Insert
    fun insert(debtEntity: DebtEntity): Long

    @androidx.room.Delete
    fun delete(debtEntity: DebtEntity)
}

class DebtsRepository @Inject constructor(
    private val debtDataSource: DebtDataSource
)
{
    suspend fun fetchDebt(debtId: Long): Debt? {
        return withContext(Dispatchers.IO) {
            debtDataSource.fetchDebt(debtId)
        }
    }

    suspend fun fetchDebts(eventId: Long): List<Debt> {
        return withContext(Dispatchers.IO) {
            debtDataSource.fetchDebts(eventId)
        }
    }

    suspend fun addDebt(debt: Debt): Long {
        return withContext(Dispatchers.IO) {
            debtDataSource.addDebt(debt)
        }
    }

    suspend fun deleteDebt(debt: Debt) {
        return withContext(Dispatchers.IO) {
            debtDataSource.deleteDebt(debt)
        }
    }
}

