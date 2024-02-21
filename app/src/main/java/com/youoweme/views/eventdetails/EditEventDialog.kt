package com.youoweme.views.eventdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.youoweme.model.event.Event

@Composable
fun EditEventDialog(event: Event, onEditEvent: (Event) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }

    var isNameValid by remember { mutableStateOf(false) }

    val context = LocalContext.current

    name = event.title

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                //.height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = if (it.length <= 20) it
                        else it.substring(0, 20)
                        isNameValid = it.isNotEmpty()
                    },
                    label = { Text("Name") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Enter person's name") },
                )

            }
        }
    }
}