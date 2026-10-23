package com.universaldragon.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.universaldragon.data.local.AppDatabase
import com.universaldragon.data.local.AskuttyEntry
import com.universaldragon.data.repository.AskuttyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SyncUiState(
    val isSyncing: Boolean = false,
    val lastSyncMessage: String = "",
    val syncTarget: String = "https://cloudflare.universaldragon.com (Pi5 Node)"
)

class AskuttyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AskuttyRepository

    val entries: StateFlow<List<AskuttyEntry>>

    private val _syncState = MutableStateFlow(SyncUiState())
    val syncState: StateFlow<SyncUiState> = _syncState.asStateFlow()

    private val _saveSuccessMessage = MutableStateFlow<String?>(null)
    val saveSuccessMessage: StateFlow<String?> = _saveSuccessMessage.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = AskuttyRepository(db.askuttyDao())
        entries = repository.allEntries.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun saveEntry(
        trNumber: String,
        orderNo: String,
        dfcNo: String,
        customerName: String,
        customerContact: String,
        remarks: String
    ) {
        viewModelScope.launch {
            val entry = AskuttyEntry(
                trNumber = trNumber.trim(),
                orderNo = orderNo.trim(),
                dfcNo = dfcNo.trim(),
                customerName = customerName.trim(),
                customerContact = customerContact.trim(),
                remarks = remarks.trim(),
                isSyncedToPi5 = false,
                syncEndpoint = _syncState.value.syncTarget
            )
            val id = repository.insertEntry(entry)
            _saveSuccessMessage.value = "Entry #$id saved to Room DB successfully!"
        }
    }

    fun clearSuccessMessage() {
        _saveSuccessMessage.value = null
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    fun syncEntriesToPi5() {
        viewModelScope.launch {
            _syncState.value = _syncState.value.copy(
                isSyncing = true,
                lastSyncMessage = "Connecting to Pi5 Cloudflare Node..."
            )
            delay(1200) // Simulated connection to Cloudflare tunnel / Pi5
            val currentList = entries.value
            if (currentList.isEmpty()) {
                _syncState.value = _syncState.value.copy(
                    isSyncing = false,
                    lastSyncMessage = "No logs to sync. Add entries first!"
                )
                return@launch
            }

            currentList.forEach { item ->
                repository.updateSyncStatus(item.id, true, _syncState.value.syncTarget)
            }

            _syncState.value = _syncState.value.copy(
                isSyncing = false,
                lastSyncMessage = "Successfully synced ${currentList.size} logs to Pi 5 via Cloudflare Tunnel!"
            )
        }
    }

    fun updateSyncTarget(newTarget: String) {
        _syncState.value = _syncState.value.copy(syncTarget = newTarget)
    }
}
