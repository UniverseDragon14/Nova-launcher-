package com.universaldragon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.universaldragon.ui.screens.AskuttyScreen
import com.universaldragon.ui.screens.EveChatScreen
import com.universaldragon.ui.screens.HomeScreen
import com.universaldragon.ui.screens.NovaChatScreen
import com.universaldragon.ui.screens.ProjectsScreen
import com.universaldragon.ui.theme.UniversalDragonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniversalDragonTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                                onNavigateToAskutty = { navController.navigate("askutty") }
                            )
                        }
                        composable("projects") {
                            ProjectsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("eve") {
                            EveChatScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("nova") {
                            NovaChatScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("askutty") {
                            AskuttyScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
