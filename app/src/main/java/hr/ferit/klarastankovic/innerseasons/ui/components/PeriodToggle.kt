package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

// Toggle switch for period tracking
@Composable
fun PeriodToggle(
    isPeriod: Boolean,
    onPeriodChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Period day?",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Switch(
            checked = isPeriod,
            onCheckedChange = onPeriodChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                checkedTrackColor = PrimaryPink,
                uncheckedThumbColor = White,
                uncheckedTrackColor = TextSecondary.copy(alpha = 0.3f)
            )
        )
    }
}