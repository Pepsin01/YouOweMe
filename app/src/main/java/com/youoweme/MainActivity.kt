package com.youoweme

import EventView
import HomeScreenView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var eventsRepository: EventsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YouOweMeTheme {
                App(eventsRepository)
            }
        }
    }
}

@Composable
fun App(eventsRepository: EventsRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable(
            route = "homeScreen",
            ) {

            HomeScreenView(
                onNavigateToEvent = { eventId -> navController.navigate("eventView/$eventId") },
                homeScreenViewModel = HomeScreenViewModel(eventsRepository)
            )
        }

        composable("eventView/{eventId}",
            arguments = listOf(navArgument("eventId") {type = NavType.IntType })) {
            backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId")
                ?: throw IllegalArgumentException("Navigated with wrong event id");

            val event: Event = eventsRepository.fetchEvent(eventId)
                ?: throw IllegalArgumentException("There is no event with id of $eventId")

            EventView(
                onNavigateToHomeScreen = { navController.navigate("homeScreen") },
                eventViewModel = EventViewModel(event)
            )
        }
    }
}
