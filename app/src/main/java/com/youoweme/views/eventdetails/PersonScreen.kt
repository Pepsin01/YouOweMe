package com.youoweme.views.eventdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.youoweme.model.Person

@Composable
fun PersonScreen(modifier: Modifier, persons: List<Person>, deletePerson: (Person) -> Unit) {
    if (persons.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No users yet! Add one by clicking the + button below.",
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inversePrimary,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface),
        ) {
            items(persons) { person ->
                PersonListItem(person, deletePerson)
            }
        }
    }
}

@Composable
fun PersonListItem(person: Person, deletePerson: (Person) -> Unit) {
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
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PersonAvatar(person = person)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = person.name, fontWeight = FontWeight.Bold)
            }
        }
    }
}

