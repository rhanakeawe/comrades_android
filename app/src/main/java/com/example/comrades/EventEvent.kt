package com.example.comrades

sealed interface EventEvent {
    object SaveEvent: EventEvent
    data class SetEventName(val eventName: String) : EventEvent
    data class SetEventDate(val eventDate: String) : EventEvent
    object ShowDialog: EventEvent
    object HideDialog: EventEvent
    data class SortEvents(val sortType: SortType) : EventEvent
    data class DeleteEvent(val event: Event) : EventEvent
}