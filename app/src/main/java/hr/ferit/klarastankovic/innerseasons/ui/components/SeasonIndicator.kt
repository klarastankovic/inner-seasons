package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

// Large season indicator with emoji and text used on Home screen
@Composable
fun SeasonIndicatorLarge(
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
                .clip(CircleShape)
                .background(White)
                .border(1.dp, Black, CircleShape),
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

// Small season indicator (cycle only) used in calendar and lists
@Composable
fun SeasonIndicatorSmall(
    season: Season,
    size: Dp = 32.dp,
    showEmoji: Boolean = true,
    modifier: Modifier = Modifier
) {
   Box(
       modifier = modifier
           .size(size)
           .clip(CircleShape)
           .background(White)
           .border(2.dp, season.color, CircleShape),
       contentAlignment = Alignment.Center
   ) {
       if (showEmoji) {
           Text(
               text = season.emoji,
               fontSize = (size.value * 0.5).sp,
               textAlign = TextAlign.Center
           )
       }
   }
}

// Season badge with name used for displaying season info
@Composable
fun SeasonBadge(
    season: Season,
    modifier: Modifier = Modifier
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
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Black
        )
    }
}