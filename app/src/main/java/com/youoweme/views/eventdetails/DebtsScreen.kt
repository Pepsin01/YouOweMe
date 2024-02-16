package com.youoweme.views.eventdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youoweme.model.Debt
import com.youoweme.model.Person

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

@Composable
fun DebtListItem(debt: Debt, debtor: Person, creditor: Person) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PersonAvatar(person = debtor)
            }
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Pays Arrow",
                modifier = Modifier.weight(1F)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(2F)
            ) {
                Text(text = debt.amount.toString(), fontSize = 20.sp)
            }
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Receives Arrow",
                modifier = Modifier.weight(1F)
            )
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PersonAvatar(person = creditor)
            }
        }
    }
}