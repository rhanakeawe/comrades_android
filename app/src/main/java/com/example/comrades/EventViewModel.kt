package com.example.comrades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModel(
    private val dao: EventDao
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.EVENT_NAME)
    private val _events = _sortType
        .flatMapLatest { _sortType ->
            when(_sortType) {
                SortType.EVENT_NAME -> dao.getEventsOrderedByName()
                SortType.EVENT_DATE -> dao.getEventsOrderedByDate()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(EventState())
    val state = combine(_state, _sortType, _events) { state, sortType, events ->
        state.copy(
            events = events,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EventState())

    fun onEvent (event: EventEvent) {
        when(event) {
            is EventEvent.DeleteEvent -> {
                viewModelScope.launch {
                    dao.deleteEvent(event.event)
                }
            }
            EventEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingEvent = false
                ) }
            }
            EventEvent.SaveEvent -> {
                val eventName = state.value.eventName
                val eventDate = state.value.eventDate

                if (eventName.isBlank() || eventDate.isBlank()) {
                    return
                }

                val event: Event = Event(
                    eventName = eventName,
                    eventDate = eventDate
                )
                viewModelScope.launch {
                    dao.upsertEvent(event)
                }

                _state.update { it.copy(
                    isAddingEvent = false,
                    eventName = "",
                    eventDate = ""
                ) }
            }
            is EventEvent.SetEventDate -> {
                _state.update { it.copy(
                    eventName = event.eventDate
                ) }
            }
            is EventEvent.SetEventName -> {
                _state.update { it.copy(
                    eventName = event.eventName
                ) }
            }
            EventEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingEvent = true
                ) }
            }
            is EventEvent.SortEvents -> {
                _sortType.value = event.sortType
            }
        }
    }
}