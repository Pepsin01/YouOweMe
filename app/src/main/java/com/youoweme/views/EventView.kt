import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.youoweme.viewmodel.EventViewModel

@Composable
fun EventView(onNavigateToHomeScreen: () -> Unit, eventViewModel: EventViewModel) {
    // TODO: implement

    val uiState by eventViewModel.uiState.collectAsState()

    Button(onClick = {
        onNavigateToHomeScreen()
    }) {
        Text(text = uiState.event?.title.toString())
    }
}