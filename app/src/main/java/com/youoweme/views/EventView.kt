package com.youoweme.views

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.youoweme.model.Person
import com.youoweme.model.Transaction
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.EventViewUiState
import com.youoweme.views.eventdetails.AddPersonDialog
import com.youoweme.views.eventdetails.AddTransactionDialog
import com.youoweme.views.eventdetails.BottomNavBar
import com.youoweme.views.eventdetails.DebtsScreen
import com.youoweme.views.eventdetails.OverviewScreen
import com.youoweme.views.eventdetails.PersonScreen
import com.youoweme.views.eventdetails.TransactionsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventView(onNavigateToHomeScreen: () -> Unit, eventViewModel: EventViewModel, navController: NavHostController = rememberNavController()) {
    val uiState by eventViewModel.uiState.collectAsState()

    // State to track whether the dialog is showing
    var isTransactionDialogShowing by remember { mutableStateOf(false) }

    // Function to show the dialog
    fun showAddTransactionDialog() {
        isTransactionDialogShowing = true
    }

    // Function to hide the dialog
    fun hideAddTransactionDialog() {
        isTransactionDialogShowing = false
    }

    var isPersonDialogShowing by remember { mutableStateOf(false) }

    fun showAddPersonDialog() {
        isPersonDialogShowing = true
    }

    fun hideAddPersonDialog() {
        isPersonDialogShowing = false
    }

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp
            ){
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
            }
        },
        floatingActionButton = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route == "transactions") {
                FloatingActionButton(onClick = {
                    showAddTransactionDialog()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
            else if (navController.currentBackStackEntryAsState().value?.destination?.route == "overview") {
                FloatingActionButton(onClick = {
                    showAddPersonDialog()
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        bottomBar = {
            Surface (shadowElevation = 4.dp){
                BottomNavBar(navController = navController)
            }
        }

    ) { innerPadding ->
        EventNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            eventViewModel = eventViewModel)

        // Show the dialog when isDialogShowing is true
        if (isTransactionDialogShowing) {
            AddTransactionDialog(
                persons = uiState.persons,
                onAddTransaction = { transaction ->
                    val newTransaction = Transaction(
                        eventId = uiState.event?.id ?: 0, //TODO: handle this better
                        payerId = transaction.payerId,
                        payeeId = transaction.payeeId,
                        amount = transaction.amount,
                        description = transaction.description
                    )

                    eventViewModel.addTransaction(newTransaction)
                    hideAddTransactionDialog()
                },
                onDismiss = {
                    hideAddTransactionDialog()
                }
            )
        }

        if (isPersonDialogShowing) {
            AddPersonDialog(
                onAddPerson = { person ->
                    val newPerson = Person(
                        eventId = uiState.event?.id ?: 0, //TODO: handle this better
                        name = person.name
                    )
                    eventViewModel.addPerson(newPerson)
                    hideAddPersonDialog()
                },
                onDismiss = {
                    hideAddPersonDialog()
                }
            )
        }

    }
}

@Composable
private fun EventNavigationGraph(navController: NavHostController, modifier: Modifier, uiState: EventViewUiState, eventViewModel: EventViewModel) {
    NavHost(navController = navController, startDestination = "overview") {
        composable("overview") {
            Column {
                /*
                OverviewScreen(
                    modifier = modifier,
                )
                 */
                PersonScreen(
                    modifier = modifier,
                    persons = uiState.persons,
                    deletePerson = eventViewModel::deletePerson
                )
            }
        }
        composable("debts") {
            DebtsScreen(
                modifier = modifier,
                debts = uiState.debts,
                persons = uiState.persons
            )
        }
        composable("transactions") {
            TransactionsScreen(
                modifier = modifier,
                transactions = uiState.transactions,
                deleteTransaction = eventViewModel::deleteTransaction,
                persons = uiState.persons
            )
        }
    }
}