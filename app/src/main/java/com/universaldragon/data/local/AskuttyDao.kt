package com.universaldragon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface AskuttyDao {
    @Query("SELECT * FROM askutty_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<AskuttyEntry>>

    @Query("SELECT * FROM askutty_entries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): AskuttyEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: AskuttyEntry): Long

    @Update
    suspend fun updateEntry(entry: AskuttyEntry): Int

    @Delete
    suspend fun deleteEntry(entry: AskuttyEntry): Int

    @Query("DELETE FROM askutty_entries WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM askutty_entries")
    suspend fun clearAllEntries(): Int

    @Query("UPDATE askutty_entries SET isSyncedToPi5 = :synced, syncEndpoint = :endpoint WHERE id = :id")
    suspend fun updateSyncStatus(id: Long, synced: Boolean, endpoint: String): Int
}


