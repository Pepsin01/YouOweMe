package com.youoweme.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.youoweme.model.Debt
import com.youoweme.model.Event
import com.youoweme.model.Transaction
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.EventViewUiState
import com.youoweme.views.eventdetails.AddTransactionDialog
import com.youoweme.views.eventdetails.BottomNavBar
import com.youoweme.views.eventdetails.DebtsScreen
import com.youoweme.views.eventdetails.OverviewScreen
import com.youoweme.views.eventdetails.TransactionsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventView(onNavigateToHomeScreen: () -> Unit, eventViewModel: EventViewModel, navController: NavHostController = rememberNavController()) {
    val uiState by eventViewModel.uiState.collectAsState()

    // State to track whether the dialog is showing
    var isDialogShowing by remember { mutableStateOf(false) }

    // Function to show the dialog
    fun showAddTransactionDialog() {
        isDialogShowing = true
    }

    // Function to hide the dialog
    fun hideAddTransactionDialog() {
        isDialogShowing = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = uiState.event?.title.toString())
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToHomeScreen() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Options"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route == "transactions") {
                FloatingActionButton(onClick = {
                    showAddTransactionDialog()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }

    ) { innerPadding ->
        EventNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            eventViewModel = eventViewModel)

        // Show the dialog when isDialogShowing is true
        if (isDialogShowing) {
            AddTransactionDialog(
                onAddTransaction = { transactionDetails ->

                    val newTransaction = Transaction(
                        eventId = uiState.event?.id ?: 0, //TODO: handle this better
                        payer = transactionDetails.payer,
                        payee = transactionDetails.payee,
                        amount = transactionDetails.amount,
                        description = transactionDetails.description
                    )

                    eventViewModel.addTransaction(newTransaction)
                    hideAddTransactionDialog()
                },
                onDismiss = {
                    hideAddTransactionDialog()
                }
            )
        }
    }
}

@Composable
private fun EventNavigationGraph(navController: NavHostController, modifier: Modifier, uiState: EventViewUiState, eventViewModel: EventViewModel) {
    NavHost(navController = navController, startDestination = "overview") {
        composable("overview") {
            OverviewScreen(modifier = modifier)
        }
        composable("debts") {
            DebtsScreen(
                modifier = modifier,
                debts = uiState.debts,
            )
        }
        composable("transactions") {
            TransactionsScreen(
                modifier = modifier,
                transactions = uiState.transactions,
                deleteTransaction = { transaction ->
                eventViewModel.deleteTransaction(transaction)
                }
            )
        }
    }
}