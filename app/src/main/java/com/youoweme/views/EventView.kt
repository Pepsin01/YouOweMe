import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.youoweme.viewmodel.EventViewModel

@Composable
fun EventView(onNavigateToHomeScreen: () -> Unit, eventViewModel: EventViewModel) {
    // TODO: implement

    Button(onClick = {
        onNavigateToHomeScreen()
    }) {
        Text(text = eventViewModel.event.id.toString())
    }
}