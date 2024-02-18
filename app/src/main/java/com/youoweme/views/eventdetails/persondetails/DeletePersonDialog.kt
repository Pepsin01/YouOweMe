package com.youoweme.views.eventdetails.persondetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.youoweme.model.person.Person

@Composable
fun DeletePersonDialog(person: Person, deletePerson: (Person) -> Unit, dismissDialog: () -> Unit) {
    AlertDialog(
        onDismissRequest = dismissDialog,
        icon = {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete Person Icon")
        },
        title = {
            Text(text = "Delete person")
        },
        text = {
            Text(text = "Are you sure you want to delete ${person.name}?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deletePerson(person)
                    dismissDialog()
                }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissDialog
            ) {
                Text(text = "Cancel")
            }
        }
    )
}