package com.youoweme.views.eventdetails.persondetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.youoweme.model.person.Person

@Composable
fun PersonListItem(person: Person, deletePerson: (Person) -> Unit, updatePerson: (Person) -> Unit) {
    var isAlertDialogShowing by remember {
        mutableStateOf(false)
    }

    var isBottomMenuVisible by remember {
        mutableStateOf(false)
    }

    val density = LocalDensity.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .clickable(onClick = { isBottomMenuVisible = !isBottomMenuVisible }),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PersonAvatar(person = person)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = person.name, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1F))
            Column {
                Text(
                    text = if (person.balance < 0) "-" + person.balance.toString() else person.balance.toString() + " €",
                    color = if (person.balance < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1F),
                shadowElevation = 4.dp
            ) {
                IconButton(
                    onClick = {
                        isBottomMenuVisible = false
                        updatePerson(person)
                    },
                ) {
                    Icon(imageVector = Icons.Outlined.Create, contentDescription = "Edit")
                }
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1F),
                shadowElevation = 4.dp
            ) {
                IconButton(
                    onClick = {
                        isAlertDialogShowing = true
                        isBottomMenuVisible = false
                    },
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                }
            }
        }
    }

    if (isAlertDialogShowing) {
        DeletePersonDialog(
            person = person,
            deletePerson = deletePerson,
            dismissDialog = { isAlertDialogShowing = false }
        )
    }
}

