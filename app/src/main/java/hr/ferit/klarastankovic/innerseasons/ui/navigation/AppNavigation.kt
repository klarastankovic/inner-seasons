package hr.ferit.klarastankovic.innerseasons.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.CalendarViewModel
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.DayLogViewModel
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.HomeViewModel
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.SettingsViewModel
import hr.ferit.klarastankovic.innerseasons.ui.screens.CalendarScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.DayLogScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.HomeScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.NoDataScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.OnboardingScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.SeasonsScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.SettingsScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.SplashScreen
import hr.ferit.klarastankovic.innerseasons.ui.screens.ViewOnlyDayLogScreen

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val DAY_LOG = "day_log/{date}"
    const val VIEW_ONLY_LOG = "view_only_log/{date}"
    const val NO_DATA = "no_data/{date}"
    const val SEASONS = "seasons"
    const val SETTINGS = "settings"

    fun getDayLogRoute(date: String) = "day_log/$date"
    fun getViewOnlyRoute(date: String) = "view_only_log/$date"
    fun getNoDataRoute(date: String) = "no_data/$date"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val calendarViewModel: CalendarViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val dayLogViewModel: DayLogViewModel = viewModel()

    LaunchedEffect(settingsViewModel.userProfile) {
        settingsViewModel.userProfile?.let {
            calendarViewModel.refreshCalendarData()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                homeViewModel = homeViewModel,
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                viewModel = settingsViewModel,
                navController = navController
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                navController = navController
            )
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(
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
            DayLogScreen(
                date = date,
                navController = navController,
                viewModel = dayLogViewModel
            )
        }

        composable(
            route = Routes.VIEW_ONLY_LOG,
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            ViewOnlyDayLogScreen(
                date = date,
                navController = navController,
                viewModel = dayLogViewModel
            )
        }

        composable(
            route = Routes.NO_DATA,
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            NoDataScreen(
                date = date,
                navController = navController
            )
        }

        composable(Routes.SEASONS) {
            SeasonsScreen(navController = navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = settingsViewModel,
                navController = navController
            )
        }
    }

}