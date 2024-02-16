package com.youoweme.views.eventdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youoweme.model.Person
import kotlin.random.Random

@Composable
fun PersonAvatar(person: Person) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                Random(person.id).let {
                    Color(
                        red = it.nextFloat(),
                        green = it.nextFloat(),
                        blue = it.nextFloat(),
                        alpha = 1f
                    )
                }
            ),
    ) {
        Text(
            text = person.name.first().toString(),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}