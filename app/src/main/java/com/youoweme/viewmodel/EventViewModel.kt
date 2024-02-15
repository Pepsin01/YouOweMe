package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youoweme.algorithms.Accountant
import com.youoweme.model.Event
import com.youoweme.model.EventsRepository
import com.youoweme.model.Debt
import com.youoweme.model.DebtsRepository
import com.youoweme.model.Person
import com.youoweme.model.PersonsRepository
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
    val persons: List<Person>
)

class EventViewModel(
    private val eventId: Int,
    private var eventsRepository: EventsRepository,
    private var debtsRepository: DebtsRepository,
    private var transactionsRepository: TransactionsRepository,
    private var personsRepository: PersonsRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(EventViewUiState(null, listOf(), listOf(), listOf()))
    val uiState: StateFlow<EventViewUiState> = _uiState.asStateFlow()
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

                _uiState.update { currState ->
                    currState.copy(
                        event = event,
                        persons = persons,
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

            updateDebts()
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

            updateDebts()
        }
    }

    fun addPerson(person: Person) {
        viewModelScope.launch {
            personsRepository.addPerson(person)

            val persons = personsRepository.fetchPersons(eventId.toLong())

            _uiState.update { currState ->
                currState.copy(
                    persons = persons,
                )
            }
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            personsRepository.deletePerson(person.id)

            val persons = personsRepository.fetchPersons(eventId.toLong())

            _uiState.update { currState ->
                currState.copy(
                    persons = persons,
                )
            }
        }
    }

    private fun updateDebts() {
        viewModelScope.launch {
            val transactions = transactionsRepository.fetchTransactions(eventId.toLong())
            for (debt in _uiState.value.debts) {
                debtsRepository.deleteDebt(debt)
            }
            var debts = accountant?.RecalculateDebts(_uiState.value.debts, transactions)
            if (debts != null) {
                for (debt in debts) {
                    debtsRepository.addDebt(debt)
                }
            }
            debts = debtsRepository.fetchDebts(eventId.toLong())


            _uiState.update { currState ->
                currState.copy(
                    debts = debts,
                )
            }
        }
    }
}