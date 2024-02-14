package com.youoweme.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class User(val id: Int, val name: String)

data class UserViewState(val isAddingUser: Boolean, val newUserName: String)

@Composable
fun UserList(users: List<User>, userViewState: UserViewState, onUserViewStateChange: (UserViewState) -> Unit, onAddButtonClick: (String) -> Unit) {
    LazyColumn {
        items(users) { user ->
            UserListItem(user = user)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            if (userViewState.isAddingUser) {
                AddUserTextField(
                    value = userViewState.newUserName,
                    onValueChange = { onUserViewStateChange(userViewState.copy(newUserName = it)) },
                    onAddButtonClick = {
                        onAddButtonClick(userViewState.newUserName)
                        onUserViewStateChange(userViewState.copy(isAddingUser = false, newUserName = ""))
                    },
                    onCancelButtonClick = {
                        onUserViewStateChange(userViewState.copy(isAddingUser = false, newUserName = ""))
                    }
                )
            } else {
                AddButton(onClick = { onUserViewStateChange(userViewState.copy(isAddingUser = true)) })
            }
        }
    }
}

@Composable
fun UserListItem(user: User) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            UserAvatar(username = user.name)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = user.name, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "+",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}

@Composable
fun AddUserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text("Enter username") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier.clickable { onAddButtonClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Add", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier.clickable { onCancelButtonClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Cancel", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun UserAvatar(username: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    ) {
        Text(
            text = username.first().toString(),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun UserListItemPreview() {
    val userList = List(5) { User(it, "User $it") }
    val userViewState = UserViewState(isAddingUser = false, newUserName = "")
    UserList(userList, userViewState, {}, onAddButtonClick = { })
}
