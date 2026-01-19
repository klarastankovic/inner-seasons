package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

/**
 * Mood selector with 5 emoji options
 * Scale: 1 = Very Bad, 2 = Bad, 3 = Neutral, 4 = Good, 5 = Very Good
 */

@Composable
fun MoodSelector(
    selectedMood: Int,
    onMoodSelected: (Int) -> Unit,
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
        Text(
            text = "Mood",
            modifier = Modifier.padding(start = 4.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoodOption(
                emoji = "😢",
                moodValue = 1,
                isSelected = selectedMood == 1,
                onClick = { onMoodSelected(1) }
            )

            MoodOption(
                emoji = "😕",
                moodValue = 2,
                isSelected = selectedMood == 2,
                onClick = { onMoodSelected(2) }
            )

            MoodOption(
                emoji = "😐",
                moodValue = 3,
                isSelected = selectedMood == 3,
                onClick = { onMoodSelected(3) }
            )

            MoodOption(
                emoji = "🙂",
                moodValue = 4,
                isSelected = selectedMood == 4,
                onClick = { onMoodSelected(4) }
            )

            MoodOption(
                emoji = "😄",
                moodValue = 5,
                isSelected = selectedMood == 5,
                onClick = { onMoodSelected(5) }
            )
        }
    }
}

@Composable
private fun MoodOption(
    emoji: String,
    moodValue: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Transparent)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = if (isSelected) 40.sp else 32.sp
        )
    }
}