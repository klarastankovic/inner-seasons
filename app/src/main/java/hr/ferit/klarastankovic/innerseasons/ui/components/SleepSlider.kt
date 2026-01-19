package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

// Sleep hours slider (0-12 hours)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepSlider(
    sleepHours: Float,
    onSleepHoursChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .background(White)
            .clip(RoundedCornerShape(12.dp))
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sleep",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Text(
                text = String.format("%.1f h", sleepHours),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = sleepHours,
            onValueChange = onSleepHoursChange,
            valueRange = 0f..12f,
            steps = 23, // 0.5 hour increments
            colors = SliderDefaults.colors(
                thumbColor = PrimaryPink,
                activeTrackColor = PrimaryPink,
                inactiveTrackColor = TextSecondary.copy(alpha = 0.3f),
                activeTickColor = Transparent,
                inactiveTickColor = Transparent
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(PrimaryPink, CircleShape)
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0 h",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Text(
                text = "12 h",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}