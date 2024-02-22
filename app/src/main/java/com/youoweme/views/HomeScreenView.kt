package com.youoweme.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youoweme.model.event.Event
import com.youoweme.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView(onNavigateToEvent: (eventId: Long) -> Unit, homeScreenViewModel: HomeScreenViewModel) {
    val uiState by homeScreenViewModel.uiState.collectAsState()
    var clicking by remember { mutableIntStateOf(0) }
    var isEventAddDialogShowing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("You Owe Me!", textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth())
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isEventAddDialogShowing = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.inverseOnSurface),
        ) {
            items(uiState.events) { event ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .clickable(onClick = { onNavigateToEvent(event.id) }),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column {
                            Text(text = event.title)
                        }
                    }
                }
            }
        }

        if (isEventAddDialogShowing) {
            AddEventDialog(
                onDismiss = { isEventAddDialogShowing = false },
                onAddEvent = { title: String -> homeScreenViewModel.addEvent(Event(title = title)) }
            )
        }
    }
}
