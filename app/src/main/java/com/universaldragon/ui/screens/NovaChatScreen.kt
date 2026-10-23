package com.universaldragon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.universaldragon.ui.theme.Cyan
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.Muted

@Composable
fun NovaChatScreen() {
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(ChatMessage("NOVA online ✅ Ask about Universal Dragon, Aslam, NOVA Core, EVE, AI, robotics, Raspberry Pi, or developer architecture.", false))) }

    fun getLocalReply(q: String): String {
        val s = q.lowercase()
        if (s.contains("universal dragon")) return "Universal Dragon is Aslam's long-term real system project: AI, robotics, Raspberry Pi, automation, NOVA Core, EVE System, and developer architecture."
        if (s.contains("aslam")) return "Aslam is the founder and builder of Universal Dragon, focused on UAE and global AI systems, robotics, automation, and practical engineering."
        if (s.contains("eve")) return "EVE is the assistant-first companion/control layer direction inside Universal Dragon: safe, useful, approval-based, and practical."
        if (s.contains("nova")) return "NOVA Core is the stable brain direction: memory, approval, rollback, safe tools, reasoning, and structured workflows."
        if (s.contains("architecture") || s.contains("developer")) return "Developer architecture means the layers of Universal Dragon: brain layer, build layer, physical layer, safety layer, and public project layer."
        return "NOVA safe reply: I can explain Universal Dragon, Aslam, NOVA Core, EVE, AI, robotics, Raspberry Pi, automation, and developer architecture."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .border(1.dp, Fire.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val bgColor = if (msg.isUser) Fire.copy(alpha = 0.1f) else Cyan.copy(alpha = 0.07f)
                val borderColor = if (msg.isUser) Fire.copy(alpha = 0.22f) else Cyan.copy(alpha = 0.18f)
                
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
                    focusedBorderColor = Fire,
                    unfocusedBorderColor = Fire.copy(alpha = 0.3f)
                ),
                placeholder = { Text("Ask NOVA something...", color = Muted) }
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
                colors = ButtonDefaults.buttonColors(containerColor = Fire.copy(alpha = 0.2f), contentColor = Fire)
            ) {
                Text("Send")
            }
        }
    }
}
