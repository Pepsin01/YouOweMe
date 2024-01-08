package com.youoweme

import EventView
import HomeScreenView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import com.youoweme.ui.theme.YouOweMeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.youoweme.model.Event
import com.youoweme.model.EventsDataSource
import com.youoweme.model.EventsRepository
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            val homeScreenViewModel = HomeScreenViewModel(EventsRepository(EventsDataSource())) //TODO: inject all using DI

            HomeScreenView(
                onNavigateToEvent = { eventId -> navController.navigate("eventView/$eventId") },
                homeScreenViewModel = homeScreenViewModel
            )
        }

        composable("eventView/{eventId}",
            arguments = listOf(navArgument("eventId") {type = NavType.IntType })) {
            backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId")
                ?: throw IllegalArgumentException("Navigated with wrong event id");

            val eventsRepository = EventsRepository(EventsDataSource()) //TODO: inject all using DI
            val event: Event = eventsRepository.fetchEvent(eventId)
                ?: throw IllegalArgumentException("There is no event with id of $eventId")

            val eventViewModel = EventViewModel(event)

            EventView(
                onNavigateToHomeScreen = { navController.navigate("homeScreen") },
                eventViewModel = eventViewModel
            )
        }
    }
}
