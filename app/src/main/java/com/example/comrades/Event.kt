package com.example.comrades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    val eventName : String,
    val eventDate : String,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
)
