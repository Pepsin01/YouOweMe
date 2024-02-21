package com.youoweme.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction
import com.youoweme.viewmodel.EventViewModel
import com.youoweme.viewmodel.EventViewUiState
import com.youoweme.views.eventdetails.persondetails.AddOrEditPersonDialog
import com.youoweme.views.eventdetails.transactiondetails.AddOrEditTransactionDialog
import com.youoweme.views.eventdetails.BottomNavBar
import com.youoweme.views.eventdetails.DebtsScreen
import com.youoweme.views.eventdetails.PersonScreen
import com.youoweme.views.eventdetails.TransactionsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventView(onNavigateToHomeScreen: () -> Unit, eventViewModel: EventViewModel, navController: NavHostController = rememberNavController()) {
    val uiState by eventViewModel.uiState.collectAsState()

    var isTransactionDialogShowing by remember { mutableStateOf(false) }

    var isPersonDialogShowing by remember { mutableStateOf(false) }

    var isEditTransactionDialogShowing by remember { mutableStateOf(false) }

    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    var isUpdatePersonDialogShowing by remember { mutableStateOf(false) }

    var personToUpdate by remember { mutableStateOf<Person?>(null) }

    var showMenu by remember { mutableStateOf(false) }

    fun editTransaction(transaction: Transaction) {
        transactionToEdit = transaction
        isEditTransactionDialogShowing = true
    }

    fun updatePerson(person: Person) {
        personToUpdate = person
        isUpdatePersonDialogShowing = true
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
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Options"
                            )
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = { /* TODO: Handle edit! */ },
                                    text = { Text("Edit") }
                                )
                                DropdownMenuItem(
                                    onClick = { /* TODO: Handle delete! */ },
                                    text = { Text("Delete") }
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route == "transactions") {
                FloatingActionButton(onClick = {
                    isTransactionDialogShowing = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
            else if (navController.currentBackStackEntryAsState().value?.destination?.route == "overview") {
                FloatingActionButton(onClick = {
                    isPersonDialogShowing = true
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
            eventViewModel = eventViewModel,
            editTransaction = ::editTransaction,
            updatePerson = ::updatePerson
        )

        // Show the dialog when isDialogShowing is true
        if (isTransactionDialogShowing) {
            AddOrEditTransactionDialog(
                editedTransaction = null,
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
                    isTransactionDialogShowing = false
                },
                onEditTransaction = {},
                onDismiss = {
                    isTransactionDialogShowing = false
                }
            )
        }

        if (isEditTransactionDialogShowing) {
            AddOrEditTransactionDialog(
                editedTransaction = transactionToEdit,
                persons = uiState.persons,
                onEditTransaction = { transaction ->
                    eventViewModel.updateTransaction(transaction)
                    isEditTransactionDialogShowing = false
                },
                onAddTransaction = {},
                onDismiss = {
                    isEditTransactionDialogShowing = false
                }
            )
        }

        if (isPersonDialogShowing) {
            AddOrEditPersonDialog(
                onAddPerson = { person ->
                    val newPerson = Person(
                        eventId = uiState.event?.id ?: 0, //TODO: handle this better
                        name = person.name
                    )
                    eventViewModel.addPerson(newPerson)
                    isPersonDialogShowing = false
                },
                onDismiss = {
                    isPersonDialogShowing = false
                },
                editedPerson = null,
                onEditPerson = {}
            )
        }

        if (isUpdatePersonDialogShowing) {
            AddOrEditPersonDialog(
                onAddPerson = {},
                onDismiss = {
                    isUpdatePersonDialogShowing = false
                },
                editedPerson = personToUpdate,
                onEditPerson = { person ->
                    eventViewModel.updatePerson(person)
                    isUpdatePersonDialogShowing = false
                }
            )
        }
    }
}

@Composable
private fun EventNavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    uiState: EventViewUiState,
    eventViewModel: EventViewModel,
    editTransaction: (Transaction) -> Unit,
    updatePerson: (Person) -> Unit
) {
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
                    deletePerson = eventViewModel::deletePerson,
                    updatePerson = updatePerson
                )
            }
        }
        composable("debts") {
            DebtsScreen(
                modifier = modifier,
                debts = uiState.debts,
                persons = uiState.persons,
                settleDebt = eventViewModel::settleDebt
            )
        }
        composable("transactions") {
            TransactionsScreen(
                modifier = modifier,
                transactions = uiState.transactions,
                deleteTransaction = eventViewModel::deleteTransaction,
                editTransaction = editTransaction,
                persons = uiState.persons
            )
        }
    }
}