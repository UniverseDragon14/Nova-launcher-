package com.universaldragon.data.repository

import com.universaldragon.data.local.AskuttyDao
import com.universaldragon.data.local.AskuttyEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AskuttyRepository(private val dao: AskuttyDao) {

    val allEntries: Flow<List<AskuttyEntry>> = dao.getAllEntries()

    suspend fun insertEntry(entry: AskuttyEntry): Long = withContext(Dispatchers.IO) {
        dao.insertEntry(entry)
    }

    suspend fun deleteEntry(entry: AskuttyEntry) = withContext(Dispatchers.IO) {
        dao.deleteEntry(entry)
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        dao.clearAllEntries()
    }

    suspend fun updateSyncStatus(id: Long, synced: Boolean, endpoint: String) = withContext(Dispatchers.IO) {
        dao.updateSyncStatus(id, synced, endpoint)
    }
}
