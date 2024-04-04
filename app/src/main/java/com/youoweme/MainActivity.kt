package com.youoweme

import com.youoweme.views.EventView
import com.youoweme.views.HomeScreenView
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.youoweme.ui.theme.YouOweMeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.youoweme.model.event.EventsRepository
import com.youoweme.model.person.PersonsRepository
import com.youoweme.model.transaction.TransactionsRepository
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.HomeScreenViewModel
import com.youoweme.viewmodel.UIState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var eventsRepository: EventsRepository
    @Inject lateinit var transactionsRepository: TransactionsRepository
    @Inject lateinit var personsRepository: PersonsRepository

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface MainActivityEntryPoint {
        fun eventViewModelFactory(): EventViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YouOweMeTheme {
                App(
                    eventsRepository,
                    transactionsRepository,
                    personsRepository
                )
            }
        }
    }
}

@Composable
fun eventViewModel(eventId: Long): EventViewModel {
    val entryPoint = EntryPointAccessors.fromActivity(
        LocalContext.current as MainActivity,
        MainActivity.MainActivityEntryPoint::class.java
    )
    val factory = entryPoint.eventViewModelFactory().create(eventId)
    val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return factory as T
        }
    }
    return viewModel(factory = viewModelFactory)
}

@Composable
fun App(eventsRepository: EventsRepository,
        transactionsRepository: TransactionsRepository,
        personsRepository: PersonsRepository
) {
    val navController = rememberNavController()
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                arguments = listOf(navArgument("eventId") {type = NavType.LongType })) {
                    backStackEntry ->
                val eventId = backStackEntry.arguments?.getLong("eventId")
                    ?: throw IllegalArgumentException("Navigated with wrong event id")

                val eventViewModel = eventViewModel(eventId)

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
                    is UIState.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            "Error: ${(uiState as UIState.Error).exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
