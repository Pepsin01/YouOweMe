package com.youoweme

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import com.youoweme.ui.theme.YouOweMeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            YouOweMeTheme {
                App()
            }
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable(
            route = "homeScreen",
            ) {
            HomeScreen(
                onNavigateToEvent = { eventId -> navController.navigate("eventView/$eventId") }
            )
        }

        composable("eventView/{eventId}",
            arguments = listOf(navArgument("eventId") {type = NavType.IntType })) {
            backStackEntry ->
                EventView(
                    onNavigateToHomeScreen = { navController.navigate("homeScreen") },
                    eventId = backStackEntry.arguments?.getInt("eventId")
                );
        }
    }
}

@Composable
fun HomeScreen(onNavigateToEvent: (eventId: Int) -> Unit) {
}

@Composable
fun EventView(onNavigateToHomeScreen: () -> Unit, eventId: Int? = null) {
}