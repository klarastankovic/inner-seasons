package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

@Composable
fun LogInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    showPlusIcon: Boolean = false
) {
    if (onClick != null) {
        Card(
            modifier = modifier
                .width(165.dp)
                .padding(2.dp),
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(
                containerColor = PrimaryPink
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            onClick = onClick
        ) {
            CardContent(label, value, showPlusIcon)
        }
    } else {
        Card(
            modifier = modifier
                .width(165.dp)
                .padding(2.dp),
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(
                containerColor = PrimaryPink
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            CardContent(label, value, showPlusIcon)
        }
    }
}

@Composable
private fun CardContent(
    label: String,
    value: String,
    showPlusIcon: Boolean
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = White
            )

            if (showPlusIcon) {
                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "+100ml",
                    fontSize = 12.sp,
                    color = White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = White
        )
    }
}