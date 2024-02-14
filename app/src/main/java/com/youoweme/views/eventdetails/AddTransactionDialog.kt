package com.youoweme.views.eventdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.youoweme.model.Transaction
import java.util.Date


@Composable
fun AddTransactionDialog(
    onAddTransaction: (transactionDetails: Transaction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        var payer by remember {
            mutableStateOf("")
        }
        var isPayerValid by remember {
            mutableStateOf(false)
        }
        var payee by remember {
            mutableStateOf("")
        }
        var isPayeeValid by remember {
            mutableStateOf(false)
        }
        var amount by remember {
            mutableStateOf("")
        }
        var isAmountValid by remember {
            mutableStateOf(false)
        }
        var description by remember {
            mutableStateOf("")
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = payer,
                    onValueChange = {
                        payer = if (it.length <= 20) it
                        else it.substring(0, 20)

                        isPayerValid = payer.isNotEmpty()
                    },
                    label = { Text("Payer") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    placeholder = { Text("Enter payer's name") }
                )
                OutlinedTextField(
                    value = payee,
                    onValueChange = {
                        payee = if (it.length <= 20) it
                        else it.substring(0, 20)

                        isPayeeValid = payee.isNotEmpty()
                    },
                    label = { Text("Payee") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    placeholder = { Text("Enter payee's name") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        isAmountValid = amount.isNotEmpty() && amount.toDoubleOrNull() != null
                    },
                    label = { Text("Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    placeholder = { Text("Enter numeric amount") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = if (it.length <= 200) it
                        else it.substring(0, 200)
                    },
                    label = { Text("Description (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Enter a description (optional)") }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            if (isPayerValid && isPayeeValid && isAmountValid) {
                                onAddTransaction(
                                    Transaction(
                                        eventId = 0,
                                        amount = amount.toDouble(),
                                        payer = payer,
                                        payee = payee,
                                        description = description
                                    )
                                )
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Add Transaction")
                    }
                }
            }
        }
    }
}
