package com.youoweme.views.eventdetails.transactiondetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.youoweme.model.transaction.Transaction

@Composable
fun DescriptionDialog(
    transaction: Transaction,
    payerName: String,
    payeeName: String,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "Info")
        },
        title = {
          Text(text = "Transaction Details")
        },
        text = {
            Text(
                text = "Payer: ${payerName}\n" +
                        "Payee: ${payeeName}\n" +
                        "Amount: ${String.format("%.2f", transaction.amount)}\n" +
                        "Description: ${transaction.description.ifEmpty { "No description"}}"
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}