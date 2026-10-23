package com.universaldragon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "askutty_entries")
data class AskuttyEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trNumber: String,
    val orderNo: String,
    val dfcNo: String,
    val customerName: String,
    val customerContact: String,
    val remarks: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSyncedToPi5: Boolean = false,
    val syncEndpoint: String = ""
)
