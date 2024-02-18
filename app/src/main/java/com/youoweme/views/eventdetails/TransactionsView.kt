package com.youoweme.views.eventdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction
import com.youoweme.views.eventdetails.transactiondetails.TransactionListItem

@Composable
fun TransactionsScreen(
    modifier: Modifier,
    transactions: List<Transaction>,
    persons: List<Person>,
    deleteTransaction: (Transaction) -> Unit,
    editTransaction: (Transaction) -> Unit
){
    if (transactions.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            contentAlignment = Alignment.Center,
        ){
            Text(
                text = "No transactions yet! Add one by clicking the + button below.",
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inversePrimary,
            )

        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface),
        ) {
            items(transactions) { transaction ->
                TransactionListItem(
                    transaction = transaction,
                    editTransaction = editTransaction,
                    deleteTransaction = deleteTransaction,
                    payer = persons.find { it.id == transaction.payerId }!!,
                    payee = persons.find { it.id == transaction.payeeId }!!
                )
            }
        }
    }
}

