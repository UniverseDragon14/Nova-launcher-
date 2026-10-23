package com.universaldragon.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universaldragon.data.local.AskuttyEntry
import com.universaldragon.ui.theme.Cyan
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.Gold
import com.universaldragon.ui.theme.Muted
import com.universaldragon.ui.viewmodel.AskuttyViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AskuttyHistoryScreen(
    viewModel: AskuttyViewModel,
    onNavigateToNewEntry: () -> Unit
) {
    val entries by viewModel.entries.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf<AskuttyEntry?>(null) }

    val filteredEntries = remember(entries, searchQuery) {
        if (searchQuery.isBlank()) entries
        else {
            entries.filter {
                it.trNumber.contains(searchQuery, ignoreCase = true) ||
                it.orderNo.contains(searchQuery, ignoreCase = true) ||
                it.dfcNo.contains(searchQuery, ignoreCase = true) ||
                it.customerName.contains(searchQuery, ignoreCase = true) ||
                it.customerContact.contains(searchQuery, ignoreCase = true) ||
                it.remarks.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(syncState.lastSyncMessage) {
        if (syncState.lastSyncMessage.isNotEmpty()) {
            Toast.makeText(context, syncState.lastSyncMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header & Quick Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Askutty Logs & History",
                    color = Gold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Room SQLite Database • ${entries.size} Saved Entries",
                    color = Muted,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onNavigateToNewEntry,
                colors = ButtonDefaults.buttonColors(containerColor = Fire),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Entry", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Entry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Pi5 Cloudflare Sync Status Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, Cyan.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (entries.all { it.isSyncedToPi5 } && entries.isNotEmpty()) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                        contentDescription = "Sync",
                        tint = if (entries.all { it.isSyncedToPi5 } && entries.isNotEmpty()) Cyan else Gold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Raspberry Pi 5 / Cloudflare Sync",
                            color = Cyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (syncState.isSyncing) "Syncing in progress..." else syncState.syncTarget,
                            color = Muted,
                            fontSize = 11.sp
                        )
                    }
                }

                FilledTonalButton(
                    onClick = { viewModel.syncEntriesToPi5() },
                    enabled = !syncState.isSyncing && entries.isNotEmpty(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Cyan.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (syncState.isSyncing) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Cyan, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Cyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync Pi 5", color = Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by TR, Order, Customer, or Remarks...", color = Muted, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Gold) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Text("✕", color = Muted, fontWeight = FontWeight.Bold)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Fire.copy(alpha = 0.3f)
            ),
            singleLine = true
        )

        // Logs List
        if (filteredEntries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .border(1.dp, Fire.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Storage, contentDescription = "Empty", tint = Muted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isEmpty()) "No Askutty logs saved yet." else "No matching entries found.",
                        color = Muted,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onNavigateToNewEntry,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Fire)
                    ) {
                        Text("Create First Entry", color = Fire)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredEntries, key = { it.id }) { entry ->
                    AskuttyLogCard(
                        entry = entry,
                        onDelete = {
                            entryToDelete = entry
                            showDeleteConfirmDialog = true
                        }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog && entryToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmDialog = false
                entryToDelete = null
            },
            title = { Text("Delete Entry?", color = Fire, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Are you sure you want to permanently delete TR: ${entryToDelete?.trNumber} (Order #${entryToDelete?.orderNo}) from the local Room database?",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        entryToDelete?.id?.let { viewModel.deleteEntry(it) }
                        showDeleteConfirmDialog = false
                        entryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteConfirmDialog = false
                    entryToDelete = null
                }) {
                    Text("Cancel", color = Muted)
                }
            }
        )
    }
}

@Composable
fun AskuttyLogCard(
    entry: AskuttyEntry,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(entry.timestamp) { dateFormat.format(Date(entry.timestamp)) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Fire.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Fire.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Fire.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "TR: ${entry.trNumber.ifEmpty { "N/A" }}",
                            color = Fire,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = if (entry.isSyncedToPi5) Cyan.copy(alpha = 0.15f) else Gold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (entry.isSyncedToPi5) "Pi5 Synced" else "Local Only",
                            color = if (entry.isSyncedToPi5) Cyan else Gold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Order & Customer Details
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Order No: ${entry.orderNo.ifEmpty { "-" }}", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("DFC No: ${entry.dfcNo.ifEmpty { "-" }}", color = Muted, fontSize = 12.sp)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(entry.customerName.ifEmpty { "No Customer Name" }, color = Cyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(entry.customerContact.ifEmpty { "No Contact" }, color = Muted, fontSize = 12.sp)
                }
            }

            if (entry.remarks.isNotEmpty()) {
                Surface(
                    color = Color.Black.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Remarks: ${entry.remarks}",
                        color = Muted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Timestamp footer
            Text(
                text = "Recorded: $formattedDate",
                color = Muted.copy(alpha = 0.7f),
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
