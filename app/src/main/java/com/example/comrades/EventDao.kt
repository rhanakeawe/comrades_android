package com.example.comrades

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Upsert
    suspend fun upsertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM event ORDER BY eventName ASC")
    fun getEventsOrderedByName(): Flow<List<Event>>

    @Query("SELECT * FROM event ORDER BY eventDate ASC")
    fun getEventsOrderedByDate(): Flow<List<Event>>

}