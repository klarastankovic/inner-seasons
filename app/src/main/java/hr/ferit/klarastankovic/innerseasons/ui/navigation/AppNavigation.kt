package hr.ferit.klarastankovic.innerseasons.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.CalendarViewModel
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.HomeViewModel
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.SettingsViewModel

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val DAY_LOG = "day_log/{date}"
    const val SEASONS = "seasons"
    const val SETTINGS = "settings"

    fun getDayLogRoute(date: String) = "day_log/$date"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val calendarViewModel: CalendarViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
    ) {
        composable(Routes.SPLASH) {
            SplashScreenPlaceholder(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreenPlaceholder(
                viewModel = homeViewModel,
                navController = navController
            )
        }

        composable(Routes.CALENDAR) {
            CalendarScreenPlaceholder(
                viewModel = calendarViewModel,
                navController = navController
            )
        }

        composable(
            route = Routes.DAY_LOG,
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            DayLogScreenPlaceholder(
                date = date,
                navController = navController
            )
        }

        composable(Routes.SEASONS) {
            SeasonsScreenPlaceholder(navController = navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreenPlaceholder(
                viewModel = settingsViewModel,
                navController = navController
            )
        }
    }

}


// PLACEHOLDER SCREENS

@Composable
fun SplashScreenPlaceholder(onNavigateToHome: () -> Unit) {
    // TODO: Implement SplashScreen
    androidx.compose.material3.Text("Splash Screen - TODO")
}

@Composable
fun HomeScreenPlaceholder(
    viewModel: HomeViewModel,
    navController: NavHostController
) {
    // TODO: Implement HomeScreen
    androidx.compose.material3.Text("Home Screen - TODO")
}

@Composable
fun CalendarScreenPlaceholder(
    viewModel: CalendarViewModel,
    navController: NavHostController
) {
    // TODO: Implement CalendarScreen
    androidx.compose.material3.Text("Calendar Screen - TODO")
}

@Composable
fun DayLogScreenPlaceholder(
    date: String,
    navController: NavHostController
) {
    // TODO: Implement DayLogScreen
    androidx.compose.material3.Text("Day Log Screen - TODO: $date")
}

@Composable
fun SeasonsScreenPlaceholder(navController: NavHostController) {
    // TODO: Implement SeasonsScreen
    androidx.compose.material3.Text("Seasons Screen - TODO")
}

@Composable
fun SettingsScreenPlaceholder(
    viewModel: SettingsViewModel,
    navController: NavHostController
) {
    // TODO: Implement SettingsScreen
    androidx.compose.material3.Text("Settings Screen - TODO")
}