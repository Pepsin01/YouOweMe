import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youoweme.model.Event
import com.youoweme.model.EventsDataSource
import com.youoweme.model.EventsRepository
import com.youoweme.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView(onNavigateToEvent: (eventId: Int) -> Unit, homeScreenViewModel: HomeScreenViewModel) {
    val homeScreenUiState by homeScreenViewModel.uiState.collectAsState()

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
                //TODO: first show dialog create new event dialog, this should be done on dialog save
                //TODO: how to sync ids nicely with db in the future?
                val id: Int = homeScreenUiState.events.maxOf { event -> event.id } + 1

                homeScreenViewModel.addEvent(Event(id, "newly added event"))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(modifier = Modifier
                .fillMaxHeight(0.9F)
                .padding(20.dp)
                .border(1.dp, Color.Black)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                    items(homeScreenUiState.events) {event ->
                        Row(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(),
                        ) {
                            Button(onClick = {
                                // beware! Doesnt work on newly added events yet, problems with ids
                                onNavigateToEvent(event.id)
                            },
                                modifier = Modifier
                                    .fillParentMaxWidth(),
                                shape = RoundedCornerShape(15)
                            ) {
                                Row {
                                    Column {
                                        Text(text = event.title)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenViewPreview() {
    HomeScreenView(
        onNavigateToEvent = {},
        homeScreenViewModel = HomeScreenViewModel(EventsRepository(EventsDataSource())) //TODO: inject all using DI
    )
}