package com.universaldragon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universaldragon.R
import kotlinx.coroutines.delay

val NeonCyan = Color(0xFF00FFD0)
val NeonGreen = Color(0xFF39FF88)
val NeonPurple = Color(0xFFFF40FF)
val HudBackground = Color(0xFF050A15)

@Composable
fun HomeScreen(
    onNavigateToProjects: () -> Unit,
    onNavigateToEve: () -> Unit,
    onNavigateToNova: () -> Unit,
    onNavigateToAskutty: () -> Unit,
    onNavigateToVoice: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1500) // Simulate loading time
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HudBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = NeonCyan,
                modifier = Modifier.size(64.dp),
                strokeWidth = 6.dp
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(HudBackground, Color.Black)
                    )
                )
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeonCyan.copy(alpha = 0.1f))
                    .border(2.dp, NeonCyan, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NOVA",
                            color = NeonCyan,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(NeonGreen)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Online", color = NeonGreen, fontSize = 12.sp)
                        }
                    }
                    Text(
                        text = "Universal Dragon Core • Living Intelligence Project",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main HUD Layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HudPanel(title = "SYSTEM FILES", borderColor = NeonCyan) {
                        HudText("qbit-nova-native-stage...")
                        HudText("voice_preview_arabella...")
                        HudText("dragon-local-voice-v1...")
                        HudText("Invoice-GPJLK05...")
                    }
                    HudPanel(title = "PENDING ACTIONS:", borderColor = NeonGreen, onClick = onNavigateToAskutty) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Patch System", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Approved", color = NeonGreen, fontSize = 8.sp, modifier = Modifier.background(NeonGreen.copy(alpha=0.2f), RoundedCornerShape(4.dp)).padding(2.dp))
                        }
                        HudText("Apply memory indexing...")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Fix Gemini", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Approved", color = NeonGreen, fontSize = 8.sp, modifier = Modifier.background(NeonGreen.copy(alpha=0.2f), RoundedCornerShape(4.dp)).padding(2.dp))
                        }
                        HudText("Root comms channel...")
                    }
                    HudPanel(title = "RECENT LOGS:", borderColor = NeonCyan) {
                        HudText("Action approved by Aslam.", color = NeonGreen)
                        HudText("Telecale connection stable")
                        HudText("SSH access granted")
                    }
                }

                // Center Column (Avatar & Voice text)
                Column(
                    modifier = Modifier
                        .weight(1.4f)
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar Hologram
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.8f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nova_avatar_1787120614779),
                            contentDescription = "NOVA Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, NeonCyan.copy(alpha=0.5f), RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Voice text output panel
                    HudPanel(
                        title = "VOICE 1: ARABELLA AI",
                        borderColor = NeonCyan,
                        onClick = onNavigateToVoice
                    ) {
                        Text(
                            text = "Tap to talk with Novakutty Arabella Human-like Voice AI (#1). Smiling, emotional, fighting tones ready.",
                            color = Color.White,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Right Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HudPanel(title = "CORE SPECS", borderColor = NeonPurple, onClick = onNavigateToProjects) {
                        HudText("Pi5 Main Node")
                        HudText("Connection: Tailscale")
                    }
                    HudPanel(title = "ADVANCED MODULES:", borderColor = NeonGreen, onClick = onNavigateToAskutty) {
                        HudText("• Askutty OCR & Room DB")
                        HudText("• NOVA3 Doctor")
                        HudText("• Dragon Nervous System Map")
                    }
                    HudPanel(title = "GATEWAY:", borderColor = NeonPurple) {
                        HudText("cloudflare.universaldragon.com")
                        HudText("quantum.universaldragon.com")
                    }
                    HudPanel(title = "VOICE AI | READY", borderColor = NeonCyan, onClick = onNavigateToVoice) {
                        Row(modifier = Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            listOf(0.4f, 0.7f, 0.3f, 1f, 0.5f, 0.8f, 0.2f, 0.9f, 0.6f).forEach { height ->
                                Box(modifier = Modifier.width(3.dp).fillMaxHeight(height).background(NeonPurple))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Input
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.DarkGray.copy(alpha = 0.3f))
                    .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .clickable { onNavigateToVoice() }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Speak with Novakutty (Voice AI)...", color = Color.LightGray, fontSize = 14.sp)
                    Icon(Icons.Default.Mic, contentDescription = "Mic", tint = NeonCyan)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HudText(text: String, color: Color = Color.LightGray) {
    Text(
        text = text,
        color = color,
        fontSize = 8.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
fun HudPanel(
    title: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(borderColor.copy(alpha = 0.05f))
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(8.dp)
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                color = borderColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        content()
    }
}
