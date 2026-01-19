package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

// Large season indicator with emoji and text used on Home screen
@Composable
fun SeasonIndicator(
    season: Season,
    cycleDay: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center,

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Today's season",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = season.displayName + " " + season.emoji,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "- Day $cycleDay of your cycle -",
                    fontSize = 20.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}