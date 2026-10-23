package com.universaldragon.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.universaldragon.ui.theme.Cyan
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.Gold
import com.universaldragon.ui.theme.Muted
import com.universaldragon.voice.NovakuttyVoiceManager
import com.universaldragon.voice.VoiceEmotion
import com.universaldragon.voice.VoicePersona

@Composable
fun VoiceAssistantScreen(voiceManager: NovakuttyVoiceManager) {
    val voiceState by voiceManager.voiceState.collectAsState()
    val context = LocalContext.current

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (isGranted) {
            voiceManager.startListening()
        }
    }

    var showElevenLabsDialog by remember { mutableStateOf(false) }
    var elevenLabsKey by remember { mutableStateOf("") }

    // Pulsating animation for the Dragon voice orb
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = if (voiceState.isListening) 1.25f else if (voiceState.isSpeaking) 1.15f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (voiceState.isListening) 500 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Novakutty Voice AI",
                    color = Gold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Arabella Human-like Synthesis & STT",
                    color = Cyan,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = { showElevenLabsDialog = true },
                colors = IconButtonDefaults.iconButtonColors(containerColor = Fire.copy(alpha = 0.15f))
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Voice Settings", tint = Gold)
            }
        }

        // Voice Persona Selector (1, 2, 3, 4)
        Text(
            text = "SELECT VOICE PERSONA (1 to 4)",
            color = Fire,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VoicePersona.values().forEach { persona ->
                val isSelected = voiceState.activePersona == persona
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Fire.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) Fire else Cyan.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { voiceManager.setPersona(persona) }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "#${persona.id}",
                            color = if (isSelected) Fire else Gold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = when (persona) {
                                VoicePersona.VOICE_1_ARABELLA -> "Arabella"
                                VoicePersona.VOICE_2_FIERCE_DRAGON -> "Fierce"
                                VoicePersona.VOICE_3_EMOTIONAL -> "Emotion"
                                VoicePersona.VOICE_4_QUANTUM_NOVA -> "Quantum"
                            },
                            color = if (isSelected) Cyan else Muted,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Emotion Mode Selector (Smiling, Emotional, Crying, Fighting, Ego)
        Text(
            text = "HUMAN EMOTION & FEELING",
            color = Cyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            VoiceEmotion.values().forEach { emotion ->
                val isSelected = voiceState.activeEmotion == emotion
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Cyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 1.5.dp else 1.dp,
                        if (isSelected) Cyan else Fire.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            voiceManager.setEmotion(emotion)
                            if (voiceState.novakuttyResponse.isNotEmpty()) {
                                voiceManager.speakText(voiceState.novakuttyResponse)
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(emotion.icon, fontSize = 16.sp)
                        Text(
                            text = emotion.label.split(" ").first(),
                            color = if (isSelected) Cyan else Muted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Center Pulsating Dragon Audio Core Orb
        Box(
            modifier = Modifier
                .size(190.dp)
                .scale(pulseScale),
            contentAlignment = Alignment.Center
        ) {
            // Glow Rings Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2.3f

                val ringColor = when {
                    voiceState.isListening -> Cyan
                    voiceState.isSpeaking -> Fire
                    else -> Gold
                }

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(ringColor.copy(alpha = 0.35f), Color.Transparent),
                        center = center,
                        radius = radius * 1.3f
                    ),
                    radius = radius * 1.3f,
                    center = center
                )

                drawCircle(
                    color = ringColor.copy(alpha = 0.6f),
                    radius = radius,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
                )
            }

            // Big Action Button
            IconButton(
                onClick = {
                    if (voiceState.isListening) {
                        voiceManager.stopListening()
                    } else if (voiceState.isSpeaking) {
                        voiceManager.stopSpeaking()
                    } else {
                        if (hasAudioPermission) {
                            voiceManager.startListening()
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                },
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = if (voiceState.isListening) listOf(Cyan, Color(0xFF005544))
                            else if (voiceState.isSpeaking) listOf(Fire, Color(0xFF551100))
                            else listOf(Color(0xFF221100), Color(0xFF0A0503))
                        )
                    )
                    .border(2.dp, if (voiceState.isListening) Cyan else Fire, CircleShape)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (voiceState.isListening) Icons.Default.GraphicEq
                        else if (voiceState.isSpeaking) Icons.Default.VolumeUp
                        else Icons.Default.Mic,
                        contentDescription = "Microphone",
                        tint = Color.White,
                        modifier = Modifier.size(46.dp)
                    )
                    Text(
                        text = if (voiceState.isListening) "LISTENING"
                        else if (voiceState.isSpeaking) "SPEAKING"
                        else "TAP TO SPEAK",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Live Status Text
        Text(
            text = voiceState.statusMessage,
            color = if (voiceState.isListening) Cyan else if (voiceState.isSpeaking) Fire else Muted,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        // Live Transcription & Response Cards
        if (voiceState.recognizedText.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Cyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("YOU SAID:", color = Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = voiceState.recognizedText,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (voiceState.novakuttyResponse.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Fire.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("NOVAKUTTY (Voice #1 Arabella):", color = Fire, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(voiceState.activeEmotion.icon, fontSize = 14.sp)
                        }

                        Row {
                            IconButton(
                                onClick = { voiceManager.speakText(voiceState.novakuttyResponse) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = "Replay", tint = Gold, modifier = Modifier.size(18.dp))
                            }
                            if (voiceState.isSpeaking) {
                                IconButton(
                                    onClick = { voiceManager.stopSpeaking() },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Stop, contentDescription = "Stop", tint = Color.Red, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = voiceState.novakuttyResponse,
                        color = Gold,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Quick Speech Prompts
        Text(
            text = "QUICK VOICE COMMANDS (TAP TO TEST)",
            color = Muted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "வணக்கம் நோவாகுட்டி" to VoiceEmotion.SMILING,
                "Pi 5 Status Check" to VoiceEmotion.EGO,
                "QBIT NOVA Mode" to VoiceEmotion.FIGHTING
            ).forEach { (cmd, emotion) ->
                FilledTonalButton(
                    onClick = {
                        voiceManager.setEmotion(emotion)
                        voiceManager.processAndRespond(cmd)
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Fire.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    Text(cmd, color = Fire, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // ElevenLabs API Settings Dialog
    if (showElevenLabsDialog) {
        AlertDialog(
            onDismissRequest = { showElevenLabsDialog = false },
            title = { Text("ElevenLabs / Cloudflare Voice Config", color = Gold, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Configure your ElevenLabs API Key for Arabella human-like cloud voice streaming or use Android Native TTS with custom emotional pitch synthesis.",
                        color = Muted,
                        fontSize = 12.sp
                    )
                    OutlinedTextField(
                        value = elevenLabsKey,
                        onValueChange = { elevenLabsKey = it },
                        label = { Text("ElevenLabs API Key (Optional)") },
                        placeholder = { Text("sk_...", color = Muted) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showElevenLabsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Fire)
                ) {
                    Text("Save & Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showElevenLabsDialog = false }) {
                    Text("Close", color = Muted)
                }
            }
        )
    }
}
