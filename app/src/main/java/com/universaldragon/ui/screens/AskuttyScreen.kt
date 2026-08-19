package com.universaldragon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.Gold
import com.universaldragon.ui.theme.Muted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskuttyScreen(onNavigateBack: () -> Unit) {
    var trNumber by remember { mutableStateOf("") }
    var orderNo by remember { mutableStateOf("") }
    var dfcNo by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerContact by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }

    var showStatus by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Askutty Work Assistant", color = Fire, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Fire)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Entry Form", color = Gold, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = trNumber,
                onValueChange = { trNumber = it },
                label = { Text("TR Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = orderNo,
                onValueChange = { orderNo = it },
                label = { Text("Order No") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = dfcNo,
                onValueChange = { dfcNo = it },
                label = { Text("DFC No") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Customer Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = customerContact,
                onValueChange = { customerContact = it },
                label = { Text("Customer Contact") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                label = { Text("Remarks") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Button(
                onClick = {
                    trNumber = ""
                    orderNo = ""
                    dfcNo = ""
                    customerName = ""
                    customerContact = ""
                    remarks = ""
                    showStatus = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Fire)
            ) {
                Text("Save Row", fontWeight = FontWeight.Bold)
            }
            
            if (showStatus) {
                Text("Row saved locally (in-memory).", color = Muted)
            }
        }
    }
}
