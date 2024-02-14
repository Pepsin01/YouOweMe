package com.youoweme.views.eventdetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.youoweme.R

@Composable
fun BottomNavBar( navController: NavHostController) {

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    val barItems = listOf(
        BarItem(
            "Overview",
            Icons.Filled.Face,
            Icons.Outlined.Face,
            "overview"
        ),
        BarItem(
            "Debts",
            ImageVector.vectorResource(R.drawable.baseline_account_balance_24),
            ImageVector.vectorResource(R.drawable.outline_account_balance_24),
            "debts"
        ),
        BarItem(
            "Transactions",
            ImageVector.vectorResource(R.drawable.baseline_payments_24),
            ImageVector.vectorResource(R.drawable.outline_payments_24),
            "transactions"
        ),
    )

    NavigationBar {
        barItems.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                selected = isSelected,
                onClick = {
                    selectedItem = index

                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false
            )
        }
    }
}

data class BarItem(
    val title : String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route : String
)