package com.universaldragon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.universaldragon.ui.screens.AskuttyHistoryScreen
import com.universaldragon.ui.screens.AskuttyScreen
import com.universaldragon.ui.screens.EveChatScreen
import com.universaldragon.ui.screens.HomeScreen
import com.universaldragon.ui.screens.NovaChatScreen
import com.universaldragon.ui.screens.ProjectsScreen
import com.universaldragon.ui.screens.VoiceAssistantScreen
import com.universaldragon.ui.theme.DarkBg
import com.universaldragon.ui.theme.Fire
import com.universaldragon.ui.theme.PanelBg
import com.universaldragon.ui.theme.TextLight
import com.universaldragon.ui.theme.UniversalDragonTheme
import com.universaldragon.ui.viewmodel.AskuttyViewModel
import com.universaldragon.voice.NovakuttyVoiceManager
import kotlinx.coroutines.launch

val NeonCyan = Color(0xFF00FFD0)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            UniversalDragonTheme(darkTheme = isDarkTheme) {
                val context = LocalContext.current
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val askuttyViewModel: AskuttyViewModel = viewModel()
                val voiceManager = remember { NovakuttyVoiceManager(context) }

                DisposableEffect(Unit) {
                    onDispose {
                        voiceManager.destroy()
                    }
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = MaterialTheme.colorScheme.surface
                        ) {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "NOVA Control",
                                color = NeonCyan,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                            HorizontalDivider(color = Fire.copy(alpha = 0.3f))
                            Spacer(Modifier.height(8.dp))
                            
                            val items = listOf(
                                "home" to "Home",
                                "voice" to "Voice AI (Novakutty #1)",
                                "projects" to "Projects",
                                "eve" to "EVE Chat",
                                "nova" to "NOVA Chat",
                                "askutty" to "Askutty Entry",
                                "askutty_logs" to "Askutty Logs / History"
                            )
                            items.forEach { (route, label) ->
                                NavigationDrawerItem(
                                    label = { Text(label) },
                                    selected = false,
                                    onClick = {
                                        navController.navigate(route) {
                                            popUpTo("home") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        scope.launch { drawerState.close() }
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedContainerColor = Color.Transparent,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                            
                            Spacer(modifier = Modifier.weight(1f))
                            HorizontalDivider(color = Fire.copy(alpha = 0.3f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Dark Mode", color = MaterialTheme.colorScheme.onSurface)
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = { isDarkTheme = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = NeonCyan,
                                        checkedTrackColor = NeonCyan.copy(alpha = 0.3f)
                                    )
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Universe Dragon", color = Fire, fontWeight = FontWeight.Bold) },
                                navigationIcon = {
                                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Fire)
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { navController.navigate("voice") }) {
                                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Fire)
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") {
                                HomeScreen(
                                    onNavigateToProjects = { navController.navigate("projects") },
                                    onNavigateToEve = { navController.navigate("eve") },
                                    onNavigateToNova = { navController.navigate("nova") },
                                    onNavigateToAskutty = { navController.navigate("askutty") },
                                    onNavigateToVoice = { navController.navigate("voice") }
                                )
                            }
                            composable("voice") {
                                VoiceAssistantScreen(voiceManager = voiceManager)
                            }
                            composable("projects") {
                                ProjectsScreen()
                            }
                            composable("eve") {
                                EveChatScreen()
                            }
                            composable("nova") {
                                NovaChatScreen()
                            }
                            composable("askutty") {
                                AskuttyScreen(
                                    viewModel = askuttyViewModel,
                                    onNavigateToHistory = { navController.navigate("askutty_logs") }
                                )
                            }
                            composable("askutty_logs") {
                                AskuttyHistoryScreen(
                                    viewModel = askuttyViewModel,
                                    onNavigateToNewEntry = { navController.navigate("askutty") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
