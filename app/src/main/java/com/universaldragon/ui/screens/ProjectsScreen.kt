package com.universaldragon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universaldragon.ui.theme.Cyan
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.Gold
import com.universaldragon.ui.theme.Muted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Projects", color = Gold, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
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
            ProjectCard(
                tag = "Verified Public Repository • Research Preview",
                title = "QBIT NOVA Native ⚛️",
                description = "Native C17 language/runtime architecture with deterministic lexer, parser, typed QIR, QBC bytecode, QVM, software state-vector simulation and approval-first execution security."
            )
            ProjectCard(
                tag = "Public Repository",
                title = "NovaKutty Dragon Brain 🧠",
                description = "Public repository for the NovaKutty Dragon Brain project. The portfolio links the repository directly rather than inventing runtime milestones."
            )
            ProjectCard(
                tag = "Public Repository",
                title = "Askutty ☁️",
                description = "Askutty public project repositories remain independently inspectable from the portfolio."
            )
            ProjectCard(
                tag = "Public Site Repository",
                title = "UDOS ⚙️",
                description = "Public UDOS site repository. Broader OS concepts remain clearly separated from what the public repository itself proves."
            )
        }
    }
}

@Composable
fun ProjectCard(tag: String, title: String, description: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Fire.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(tag, color = Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Text(title, color = Fire, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Text(description, color = Muted, fontSize = 14.sp)
        }
    }
}
