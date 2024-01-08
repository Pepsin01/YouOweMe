package com.youoweme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youoweme.model.Event
import com.youoweme.model.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Collections
import javax.inject.Inject

data class EventViewUiState(
    val event: Event?
)

class EventViewModel(
    private val eventId: Int,
    private var eventsRepository: EventsRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(EventViewUiState(null))
    val uiState: StateFlow<EventViewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val event = eventsRepository.fetchEvent(eventId)
                    ?: throw IllegalArgumentException("There is no event with id of $eventId")

                _uiState.update { currState ->
                    currState.copy(event = event)
                }
            }
            catch(e: Exception) {
                // TODO: ui that the IO request or whatever failed
                // TODO: do this construct everywhere where repository is used from a ViewModel
            }
        }
    }
}