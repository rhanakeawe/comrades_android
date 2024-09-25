package com.example.comrades

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Event::class],
    version = 1
)
abstract class EventDatabase : RoomDatabase(){

    abstract val dao: EventDao
}