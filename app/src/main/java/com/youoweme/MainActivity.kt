package com.youoweme

import com.youoweme.views.EventView
import com.youoweme.views.HomeScreenView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import com.youoweme.ui.theme.YouOweMeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.youoweme.model.debt.DebtsRepository
import com.youoweme.model.event.EventsRepository
import com.youoweme.model.person.PersonsRepository
import com.youoweme.model.transaction.TransactionsRepository
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.HomeScreenViewModel
import com.youoweme.viewmodel.UIState
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
                ?: throw IllegalArgumentException("Navigated with wrong event id")

            val eventViewModel = remember(eventId) {
                EventViewModel(
                    eventId,
                    eventsRepository,
                    debtsRepository,
                    transactionsRepository,
                    personsRepository
                )
            }
            val uiState by eventViewModel.uiState.collectAsState()

            when (uiState) {
                is UIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UIState.Success -> EventView(
                    onNavigateToHomeScreen = { navController.navigate("homeScreen") },
                    eventViewModel = eventViewModel,
                    uiState = uiState as UIState.Success
                )
                is UIState.Error -> Text("Error: ${(uiState as UIState.Error).exception.message}")
            }
        }
    }
}
