package com.youoweme.views.eventdetails

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.youoweme.model.Person
import com.youoweme.model.Transaction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    persons: List<Person>,
    onAddTransaction: (transactionDetails: Transaction) -> Unit,
    onDismiss: () -> Unit
) {
    val blankPerson = Person(
        0,
        "None",
        id = -1
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        var payer by remember {
            mutableStateOf(blankPerson)
        }
        var payerDropdownExpanded by remember {
            mutableStateOf(false)
        }
        var payee by remember {
            mutableStateOf(blankPerson)
        }
        var payeeDropdownExpanded by remember {
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
        val context = LocalContext.current


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
                ExposedDropdownMenuBox(
                    modifier = Modifier.padding(16.dp),
                    expanded = payerDropdownExpanded,
                    onExpandedChange = {
                        payerDropdownExpanded = !payerDropdownExpanded
                    }) {
                    OutlinedTextField(
                        value = payer.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = payerDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = payerDropdownExpanded,
                        onDismissRequest = { payerDropdownExpanded = false },
                    ) {
                        persons.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.name) },
                                onClick = {
                                    payer = item
                                    payerDropdownExpanded = false
                                },
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    modifier = Modifier.padding(16.dp),
                    expanded = payeeDropdownExpanded,
                    onExpandedChange = {
                        payeeDropdownExpanded = !payeeDropdownExpanded
                    }) {
                    OutlinedTextField(
                        value = payee.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = payeeDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = payeeDropdownExpanded,
                        onDismissRequest = { payeeDropdownExpanded = false },
                    ) {
                        persons.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.name) },
                                onClick = {
                                    payee = item
                                    payeeDropdownExpanded = false
                                },
                            )
                        }
                    }
                }
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
                            if (payer.id != payee.id && payer.id != (-1).toLong() && payee.id != (-1).toLong() && isAmountValid) {
                                onAddTransaction(
                                    Transaction(
                                        eventId = 0,
                                        amount = amount.toDouble(),
                                        payerId = payer.id,
                                        payeeId = payee.id,
                                        description = description
                                    )
                                )
                            }
                            else if(payer.id == payee.id) {
                                Toast.makeText(context, "Payer and payee cannot be the same", Toast.LENGTH_SHORT).show()
                            }
                            else if(payer.id == (-1).toLong() || payee.id == (-1).toLong()) {
                                Toast.makeText(context, "Payer and payee cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(context, "Amount cannot be empty or non-numeric", Toast.LENGTH_SHORT).show()
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
