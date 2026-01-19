package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black

// Season badge with name used for displaying season info
@Composable
fun SeasonBadge(
    season: Season,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(Transparent)
            .padding(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = season.displayName + " " + season.emoji,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = Black
        )
    }
}