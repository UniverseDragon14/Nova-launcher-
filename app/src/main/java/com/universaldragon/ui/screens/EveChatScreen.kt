package com.universaldragon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universaldragon.ui.theme.Cyan
import com.universaldragon.ui.theme.Muted

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EveChatScreen(onNavigateBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(ChatMessage("EVE online ✅ Ask about EVE System, Universal Dragon, NOVA Core, assistant-based phone, robotics, or developer architecture.", false))) }

    fun getLocalReply(q: String): String {
        val s = q.lowercase()
        if (s.contains("universal dragon")) return "Universal Dragon is Aslam's main project identity: a practical AI, robotics, automation, Raspberry Pi, NOVA, and EVE system direction."
        if (s.contains("nova")) return "NOVA Core is the stable brain direction. EVE is the assistant/companion control layer built on that vision."
        if (s.contains("phone") || s.contains("mobile")) return "EVE Mobile means the phone becomes assistant-based instead of app-based: EVE in front, apps and tools underneath, with safe approval."
        if (s.contains("eve")) return "EVE is designed as a real working assistant layer: memory, state, approval, rollback, safe actions, and practical help."
        if (s.contains("architecture") || s.contains("developer")) return "Universal Dragon developer architecture includes brain, app, web, mobile, robotics, hardware, safety, rollback, and public project layers."
        return "EVE safe reply: I can explain EVE System, NOVA Core, Universal Dragon, assistant-based phone, robotics, Raspberry Pi, automation, and developer architecture."
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EVE Chat", color = Cyan, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Cyan)
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
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .border(1.dp, Cyan.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val bgColor = if (msg.isUser) Color(0xFFFF7A1A).copy(alpha = 0.1f) else Cyan.copy(alpha = 0.07f)
                    val borderColor = if (msg.isUser) Color(0xFFFF7A1A).copy(alpha = 0.22f) else Cyan.copy(alpha = 0.18f)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = bgColor,
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = msg.text,
                                color = if (msg.isUser) Color.White else Cyan.copy(alpha = 0.8f),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Cyan,
                        unfocusedBorderColor = Cyan.copy(alpha = 0.3f)
                    ),
                    placeholder = { Text("Ask EVE something...", color = Muted) }
                )
                Button(
                    onClick = {
                        if (input.isNotBlank()) {
                            val userMsg = input
                            input = ""
                            messages = messages + ChatMessage(userMsg, true)
                            messages = messages + ChatMessage(getLocalReply(userMsg), false)
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cyan.copy(alpha = 0.2f), contentColor = Cyan)
                ) {
                    Text("Send")
                }
            }
        }
    }
}
