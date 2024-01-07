package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import com.youoweme.model.Event
import com.youoweme.model.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Collections

data class HomeScreenUiState(
    var events: List<Event>
)

class HomeScreenViewModel(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    fun addEvent(event: Event) {
        _uiState.update { currState ->
            val newEvents = currState.events.toMutableList()
            newEvents.add(event)
            currState.copy(events = Collections.unmodifiableList(newEvents))
        }
    }

    private val _uiState = MutableStateFlow(HomeScreenUiState(listOf()))
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        _uiState.value.events = eventsRepository.fetchEvents()
    }
}