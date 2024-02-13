package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youoweme.model.Event
import com.youoweme.model.EventsRepository
import com.youoweme.model.Debt
import com.youoweme.model.DebtsRepository
import com.youoweme.model.Transaction
import com.youoweme.model.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventViewUiState(
    val event: Event?,
    val debts: List<Debt>,
    val transactions: List<Transaction>,
)

class EventViewModel(
    private val eventId: Int,
    private var eventsRepository: EventsRepository,
    private var debtsRepository: DebtsRepository,
    private var transactionsRepository: TransactionsRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(EventViewUiState(null, listOf(), listOf()))
    val uiState: StateFlow<EventViewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val event = eventsRepository.fetchEvent(eventId)
                    ?: throw IllegalArgumentException("There is no event with id of $eventId")

                val debts = debtsRepository.fetchDebts(eventId.toLong())

                val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

                _uiState.update { currState ->
                    currState.copy(
                        event = event,
                        debts = debts,
                        transactions = transactions,
                    )
                }
            }
            catch(e: Exception) {
                // TODO: ui that the IO request or whatever failed
                // TODO: do this construct everywhere where repository is used from a ViewModel
            }
        }
    }

    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            debtsRepository.addDebt(debt)

            val debts = debtsRepository.fetchDebts(eventId.toLong())

            _uiState.update { currState ->
                currState.copy(
                    debts = debts,
                )
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.addTransaction(transaction)

            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

            _uiState.update { currState ->
                currState.copy(
                    transactions = transactions,
                )
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.deleteTransaction(transaction)

            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

            _uiState.update { currState ->
                currState.copy(
                    transactions = transactions,
                )
            }
        }
    }
}