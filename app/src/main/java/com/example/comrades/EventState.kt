package com.example.comrades

data class EventState(
    val events: List<Event> = emptyList(),
    val eventName: String = "",
    val eventDate: String = "",
    val isAddingEvent: Boolean = false,
    val sortType: SortType = SortType.EVENT_NAME
)
