package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youoweme.algorithms.Accountant
import com.youoweme.model.event.Event
import com.youoweme.model.event.EventsRepository
import com.youoweme.model.debt.Debt
import com.youoweme.model.debt.DebtsRepository
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
    val persons: List<Person>
)

sealed class UIState {
    data object Loading : UIState()
    data class Success(val event: EventViewUiState) : UIState()
    data class Error(val exception: Exception) : UIState()
}

class EventViewModel(
    private val eventId: Int, //TODO: Proc to neni long a volas vsude toLong pak ??
    private var eventsRepository: EventsRepository,
    private var debtsRepository: DebtsRepository,
    private var transactionsRepository: TransactionsRepository,
    private var personsRepository: PersonsRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    private var accountant: Accountant? = null //TODO: do this in a better way

    init {
        viewModelScope.launch {
            try {
                val event = eventsRepository.fetchEvent(eventId)
                    ?: throw IllegalArgumentException("There is no event with id of $eventId")

                val persons = personsRepository.fetchPersons(eventId.toLong())

                val debts = debtsRepository.fetchDebts(eventId.toLong())

                val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

                accountant = Accountant(eventId.toLong()) //TODO: do this in a better way

                _uiState.value = UIState.Success(
                    EventViewUiState(
                        event = event,
                        persons = persons,
                        debts = debts,
                        transactions = transactions,
                    )
                )
            }
            catch(e: Exception) {
                _uiState.value = UIState.Error(e)
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
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            debts = debts,
                        )
                    )
                } else {
                    currState
                }
            }
        }
    }

    fun settleDebt(debt: Debt) {
        viewModelScope.launch {
            addTransaction(
                Transaction(
                    eventId = eventId.toLong(),
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

            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

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

            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

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

            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

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

            val persons = personsRepository.fetchPersons(eventId.toLong())

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

            val persons = personsRepository.fetchPersons(eventId.toLong())
            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

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

            val persons = personsRepository.fetchPersons(eventId.toLong())

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
        // TODO: Also delete everything related to this event
        viewModelScope.launch {
            eventsRepository.deleteEvent(event)
        }
    }

    //TODO: this should be done in a better way
    private fun updateDebts() {
        viewModelScope.launch {
            val persons = personsRepository.fetchPersons(eventId.toLong());
            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())

            var debts = accountant?.getDebts(persons)
            if (debts != null) {
                for (debt in debts) {
                    debtsRepository.addDebt(debt)
                }
            }
            debts = debtsRepository.fetchDebts(eventId.toLong())


            _uiState.update { currState ->
                if (currState is UIState.Success) {
                    UIState.Success(
                        currState.event.copy(
                            debts = debts,
                        )
                    )
                } else {
                    currState
                }
            }
        }
    }
}