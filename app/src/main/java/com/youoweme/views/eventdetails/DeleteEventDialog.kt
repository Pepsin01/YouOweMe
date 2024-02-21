package com.youoweme.views.eventdetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.youoweme.model.event.Event
import com.youoweme.model.person.Person

@Composable
fun DeleteEventDialog(event: Event, deleteEvent: (Event) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete Event Icon")
        },
        title = {
            Text(text = "Delete event")
        },
        text = {
            Text(text = "Are you sure you want to delete event ${event.title}?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteEvent(event)
                    onDismiss()
                }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        }
    )
}