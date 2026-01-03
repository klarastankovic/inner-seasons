package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

// Water intake counter with +100ml button
@Composable
fun WaterIntakeCounter(
    waterIntakeMl: Int,
    onWaterIntakeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Water intake (ml)",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "$waterIntakeMl ml",
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = TextPrimary,
                    disabledBorderColor = TextSecondary.copy(alpha = 0.3f)
                ),
                enabled = false,
                singleLine = true
            )

            Button(
                onClick = { onWaterIntakeChange(waterIntakeMl + 100) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPink,
                    contentColor = White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text("100 ml")
            }
        }
    }
}