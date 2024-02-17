package com.youoweme.views.eventdetails.transactiondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youoweme.model.person.Person
import com.youoweme.model.transaction.Transaction
import com.youoweme.views.eventdetails.persondetails.PersonAvatar

@Composable
fun TransactionListItem(transaction: Transaction, deleteTransaction: (Transaction) -> Unit, payer: Person, payee: Person) {
    var isAlertDialogShowing by remember { mutableStateOf(false) }

    fun showAlertDialog() {
        isAlertDialogShowing = true
    }

    fun hideAlertDialog() {
        isAlertDialogShowing = false
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PersonAvatar(person = payer)
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
                Text(text = transaction.amount.toString(), fontSize = 20.sp)
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
                PersonAvatar(person = payee)
            }
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer,
                shadowElevation = 4.dp,
                modifier = Modifier.size(35.dp)
            )
            {
                IconButton(
                    onClick = { showAlertDialog() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Transaction",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
    if (isAlertDialogShowing) {
        DeleteDialog(
            onDismissRequest = { hideAlertDialog() },
            onConfirmation = {
                deleteTransaction(transaction)
                hideAlertDialog()
            }
        )
    }
}