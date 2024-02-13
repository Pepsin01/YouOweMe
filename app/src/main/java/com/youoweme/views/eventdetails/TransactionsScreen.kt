package com.youoweme.views.eventdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youoweme.model.Transaction

@Composable
fun TransactionsScreen(modifier: Modifier, transactions: List<Transaction>, deleteTransaction: (Transaction) -> Unit){
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(transactions) { transaction ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(
                    Modifier.weight(1F),
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Payer Icon",
                    )
                    Text(text = transaction.payer, fontSize = 10.sp)
                }
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Pays Arrow",
                    Modifier.weight(1F)
                )
                Text(text = transaction.amount.toString(), fontSize = 20.sp)
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Receives Arrow",
                    Modifier.weight(1F)
                )
                Column(
                    Modifier.weight(1F)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Payee Icon",
                    )
                    Text(text = transaction.payee, fontSize = 10.sp)
                }
                IconButton(
                    onClick = { deleteTransaction(transaction) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Transaction",
                    )
                }
            }
        }
    }
}