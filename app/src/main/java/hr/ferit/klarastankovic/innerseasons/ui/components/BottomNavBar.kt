package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.ferit.klarastankovic.innerseasons.ui.navigation.Routes
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .background(BackgroundWhite)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            AddLogFAB(
                onClick = {
                    val todayDate = java.time.LocalDate.now().toString()

                    navController.navigate(Routes.getDayLogRoute(todayDate)) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                modifier = Modifier.size(50.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentRoute != Routes.HOME) {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.HOME) { inclusive = true }
                                    }
                                }
                            }
                        )
                    },
                tint = if (currentRoute == Routes.HOME) PrimaryPink else TextSecondary
            )
            Text(
                "Home",
                color = if (currentRoute == Routes.HOME) PrimaryPink else TextSecondary
            )
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calendar",
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentRoute != Routes.CALENDAR) {
                                    navController.navigate(Routes.CALENDAR) {
                                        popUpTo(Routes.CALENDAR) { inclusive = true }
                                    }
                                }
                            }
                        )
                    },
                tint = if (currentRoute == Routes.CALENDAR) PrimaryPink else TextSecondary
            )
            Text(
                "Calendar",
                color = if (currentRoute == Routes.CALENDAR) PrimaryPink else TextSecondary
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentRoute != Routes.SETTINGS) {
                                    navController.navigate(Routes.SETTINGS) {
                                        popUpTo(Routes.SETTINGS) { inclusive = true }
                                    }
                                }
                            }
                        )
                    },
                tint = if (currentRoute == Routes.SETTINGS) PrimaryPink else TextSecondary
            )
            Text(
                "Settings",
                color = if (currentRoute == Routes.SETTINGS) PrimaryPink else TextSecondary
            )
        }
    }
}