package com.youoweme.views.eventdetails.debtdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youoweme.model.debt.Debt
import com.youoweme.model.person.Person
import com.youoweme.views.eventdetails.persondetails.PersonAvatar

@Composable
fun DebtListItem(debt: Debt, debtor: Person, creditor: Person, settleDebt: (Debt) -> Unit){

    var isBottomMenuVisible by remember { mutableStateOf(false) }

    var isAlertDialogShowing by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .clickable(onClick = { isBottomMenuVisible = !isBottomMenuVisible }),
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

    AnimatedVisibility(
        visible = isBottomMenuVisible,
        enter = slideInVertically {
            with(density) { 0.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Bottom
        ),
        exit = slideOutVertically{
            with(density) { 0.dp.roundToPx() }
        } + shrinkVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1F),
                shadowElevation = 4.dp
            ) {
                TextButton(onClick = {
                    isAlertDialogShowing = true
                    isBottomMenuVisible = false
                    }
                ) {
                    Text(
                        text = "Settle this debt",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }

    if (isAlertDialogShowing) {
        SettleDebtAlertDialog(
            debt = debt,
            debtor = debtor,
            creditor = creditor,
            onSettleDebt = {
                isAlertDialogShowing = false
                settleDebt(it)
            },
            onDismissRequest = { isAlertDialogShowing = false }
        )
    }
}