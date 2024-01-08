package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youoweme.model.Event
import com.youoweme.model.EventsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

data class HomeScreenUiState(
    var events: List<Event>
)

@ViewModelScoped
class HomeScreenViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState(listOf()))
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currState ->
                currState.copy(events = eventsRepository.fetchEvents())
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            event.id = eventsRepository.addEvent(event)
        }

        _uiState.update { currState ->
            val newEvents = currState.events.toMutableList()
            newEvents.add(event)
            currState.copy(events = Collections.unmodifiableList(newEvents))
        }
    }
}