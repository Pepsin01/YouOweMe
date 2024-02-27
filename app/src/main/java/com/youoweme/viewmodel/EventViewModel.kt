package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youoweme.algorithms.Accountant
import com.youoweme.model.event.Event
import com.youoweme.model.event.EventsRepository
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.model.person.PersonsRepository
import com.youoweme.model.transaction.Transaction
import com.youoweme.model.transaction.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventViewUiState(
    val event: Event?,
    val debts: List<Debt>,
    val transactions: List<Transaction>,
    val persons: List<Person>,
)

sealed class UIState {
    data object Loading : UIState()
    data class Success(val event: EventViewUiState) : UIState()
    data class Error(val exception: Exception) : UIState()
}

class EventViewModel(
    private val eventId: Long,
    private var eventsRepository: EventsRepository,
    private var transactionsRepository: TransactionsRepository,
    private var personsRepository: PersonsRepository,
    private val accountant: Accountant = Accountant()
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val event = eventsRepository.fetchEvent(eventId)
                    ?: throw IllegalArgumentException("There is no event with id of $eventId")

                val persons = personsRepository.fetchPersons(eventId)

                val transactions = transactionsRepository.fetchTransactions(eventId)

                _uiState.value = UIState.Success(
                    EventViewUiState(
                        event = event,
                        persons = persons,
                        debts = listOf(),
                        transactions = transactions,
                    )
                )

                updateDebts()
            }
            catch(e: Exception) {
                _uiState.value = UIState.Error(e)
                // TODO: ui that the IO request or whatever failed
                // TODO: do this construct everywhere where repository is used from a ViewModel
            }
        }
    }

    fun settleDebt(debt: Debt) {
        viewModelScope.launch {
            addTransaction(
                Transaction(
                    eventId = eventId,
                    amount = debt.amount,
                    payerId = debt.debtorId,
                    payeeId = debt.creditorId,
                    description = "Settling a debt"
                )
            )
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.addTransaction(transaction)

            val transactions = transactionsRepository.fetchTransactions(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            transactions = transactions,
                        )
                    )
                } else {
                    currState
                }
            }
            updateDebts()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.deleteTransaction(transaction)

            val transactions = transactionsRepository.fetchTransactions(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            transactions = transactions,
                        )
                    )
                } else {
                    currState
                }
            }
            updateDebts()
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionsRepository.updateTransaction(transaction)

            val transactions = transactionsRepository.fetchTransactions(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            transactions = transactions,
                        )
                    )
                } else {
                    currState
                }
            }

            updateDebts()
        }
    }

    fun addPerson(person: Person) {
        viewModelScope.launch {
            personsRepository.addPerson(person)

            val persons = personsRepository.fetchPersons(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            persons = persons,
                        )
                    )
                } else {
                    currState
                }
            }
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {

            for (transaction in (_uiState.value as UIState.Success).event.transactions) {
                if (transaction.payerId == person.id || transaction.payeeId == person.id) {
                    transactionsRepository.deleteTransaction(transaction)
                }
            }

            personsRepository.deletePerson(person.id)

            val persons = personsRepository.fetchPersons(eventId)
            val transactions = transactionsRepository.fetchTransactions(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            persons = persons,
                            transactions = transactions,
                        )
                    )
                } else {
                    currState
                }
            }
            updateDebts()
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            personsRepository.updatePerson(person)

            val persons = personsRepository.fetchPersons(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            persons = persons,
                        )
                    )
                } else {
                    currState
                }
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventsRepository.updateEvent(event)

            val updatedEvent = eventsRepository.fetchEvent(eventId)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            event = updatedEvent,
                        )
                    )
                } else {
                    currState
                }
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventsRepository.deleteEvent(event)
            personsRepository.fetchPersons(eventId.toLong()).forEach {
                personsRepository.deletePerson(it.id)
            }
            transactionsRepository.fetchTransactions(eventId.toLong()).forEach {
                transactionsRepository.deleteTransaction(it)
            }
        }
    }

    private fun updateDebts() {
        viewModelScope.launch {
            val persons = personsRepository.fetchPersons(eventId);
            val transactions = transactionsRepository.fetchTransactions(eventId)

            val debts = accountant.getDebts(persons, transactions)

            accountant.updateBalances(persons, transactions)

            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            persons = persons,
                            debts = debts
                        )
                    )
                } else {
                    currState
                }
            }
        }
    }
}