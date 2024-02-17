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
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.views.eventdetails.debtdetails.DebtListItem

@Composable
fun DebtsScreen(modifier: Modifier, debts: List<Debt>, persons: List<Person>) {
    if (debts.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            contentAlignment = Alignment.Center,
        ){
            Text(
                text = "No debts yet! Start by adding a transaction in the Transactions tab.",
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(debts) { debt ->
                DebtListItem(
                    debt,
                    persons.find { it.id == debt.debtorId }!!,
                    persons.find { it.id == debt.creditorId }!!
                )
            }
        }
    }
}

