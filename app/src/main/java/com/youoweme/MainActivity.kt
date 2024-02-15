package com.youoweme

import com.youoweme.views.EventView
import com.youoweme.views.HomeScreenView
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
import com.youoweme.model.DebtsRepository
import com.youoweme.model.EventsRepository
import com.youoweme.model.PersonsRepository
import com.youoweme.model.TransactionsRepository
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var eventsRepository: EventsRepository
    @Inject lateinit var debtsRepository: DebtsRepository
    @Inject lateinit var transactionsRepository: TransactionsRepository
    @Inject lateinit var personsRepository: PersonsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YouOweMeTheme {
                App(
                    eventsRepository,
                    debtsRepository,
                    transactionsRepository,
                    personsRepository
                )
            }
        }
    }
}

@Composable
fun App(eventsRepository: EventsRepository,
        debtsRepository: DebtsRepository,
        transactionsRepository: TransactionsRepository,
        personsRepository: PersonsRepository
) {
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

            EventView(
                onNavigateToHomeScreen = { navController.navigate("homeScreen") },
                eventViewModel = EventViewModel(
                    eventId,
                    eventsRepository,
                    debtsRepository,
                    transactionsRepository,
                    personsRepository
                ),
            )
        }
    }
}
