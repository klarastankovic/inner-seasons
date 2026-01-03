package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hr.ferit.klarastankovic.innerseasons.ui.navigation.Routes
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Home") },
            selected = currentRoute == Routes.HOME,
            onClick = {
                if (currentRoute != Routes.HOME) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryPink,
                selectedTextColor = PrimaryPink,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = PrimaryPink.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Calendar") },
            selected = currentRoute == Routes.CALENDAR,
            onClick = {
                if (currentRoute != Routes.CALENDAR) {
                    navController.navigate(Routes.CALENDAR) {
                        popUpTo(Routes.CALENDAR) { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryPink,
                selectedTextColor = PrimaryPink,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = PrimaryPink.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Settings") },
            selected = currentRoute == Routes.SETTINGS,
            onClick = {
                if (currentRoute != Routes.SETTINGS) {
                    navController.navigate(Routes.SETTINGS) {
                        popUpTo(Routes.SETTINGS) { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryPink,
                selectedTextColor = PrimaryPink,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary,
                indicatorColor = PrimaryPink.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun AddLogFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = PrimaryPink,
        contentColor = White
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Log",
            modifier = Modifier.size(28.dp)
        )
    }
}