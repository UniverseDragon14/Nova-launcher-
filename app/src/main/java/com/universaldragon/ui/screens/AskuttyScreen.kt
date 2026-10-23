package com.universaldragon.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universaldragon.ui.theme.Cyan
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.Gold
import com.universaldragon.ui.theme.Muted
import com.universaldragon.ui.viewmodel.AskuttyViewModel

@Composable
fun AskuttyScreen(
    viewModel: AskuttyViewModel,
    onNavigateToHistory: () -> Unit
) {
    var trNumber by remember { mutableStateOf("") }
    var orderNo by remember { mutableStateOf("") }
    var dfcNo by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerContact by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }

    val entries by viewModel.entries.collectAsState()
    val successMsg by viewModel.saveSuccessMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(successMsg) {
        if (successMsg != null) {
            Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header with History Shortcut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Askutty Work Assistant", color = Gold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Local SQLite Persistence Engine", color = Muted, fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = onNavigateToHistory,
                border = BorderStroke(1.dp, Cyan),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Cyan),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = "History", modifier = Modifier.size(16.dp), tint = Cyan)
                Spacer(modifier = Modifier.width(6.dp))
                Text("View Logs (${entries.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (successMsg != null) {
            Surface(
                color = Cyan.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, Cyan.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = Cyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(successMsg ?: "", color = Cyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    TextButton(onClick = onNavigateToHistory) {
                        Text("Open Logs", color = Gold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Text("NEW DATA ENTRY", color = Fire, fontSize = 13.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = trNumber,
            onValueChange = { trNumber = it },
            label = { Text("TR Number") },
            leadingIcon = { Icon(Icons.Default.Receipt, contentDescription = null, tint = Fire) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = orderNo,
                onValueChange = { orderNo = it },
                label = { Text("Order No") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = dfcNo,
                onValueChange = { dfcNo = it },
                label = { Text("DFC No") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Gold) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = customerContact,
            onValueChange = { customerContact = it },
            label = { Text("Customer Contact") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Gold) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = remarks,
            onValueChange = { remarks = it },
            label = { Text("Work Notes / Remarks") },
            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = Muted) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(12.dp),
            maxLines = 3
        )

        Button(
            onClick = {
                if (trNumber.isBlank() && orderNo.isBlank() && customerName.isBlank()) {
                    Toast.makeText(context, "Please enter at least TR No, Order No, or Customer Name", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.saveEntry(
                    trNumber = trNumber,
                    orderNo = orderNo,
                    dfcNo = dfcNo,
                    customerName = customerName,
                    customerContact = customerContact,
                    remarks = remarks
                )
                // Clear fields
                trNumber = ""
                orderNo = ""
                dfcNo = ""
                customerName = ""
                customerContact = ""
                remarks = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Fire)
        ) {
            Icon(Icons.Default.Save, contentDescription = "Save")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save to Local Room DB", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
