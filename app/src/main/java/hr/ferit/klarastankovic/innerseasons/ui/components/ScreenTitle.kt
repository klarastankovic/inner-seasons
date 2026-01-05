package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black

@Composable
fun ScreenTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        ),
        color = Black,
        modifier = modifier.padding(24.dp)
    )
}