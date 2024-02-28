package com.youoweme.views.eventdetails.debtdetails

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.youoweme.R
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person

@Composable
fun SettleDebtAlertDialog(
    debt: Debt,
    debtor: Person,
    creditor: Person,
    onSettleDebt: (Debt) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        icon = {
            ImageVector.vectorResource(R.drawable.outline_account_balance_24)
        },
        title = {
            Text(text = "Settling Debt")
        },
        text = {
            Text(
                text = "You are about to settle a debt of ${String.format("%.2f", debt.amount)} from ${debtor.name} to " +
                        "${creditor.name}. Are you sure you want to proceed?"
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSettleDebt(debt) }
            ) {
                Text(text = "Confirm")
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = "Dismiss")
            }
        }
    )
}