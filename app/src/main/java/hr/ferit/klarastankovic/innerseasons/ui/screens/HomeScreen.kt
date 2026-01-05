package hr.ferit.klarastankovic.innerseasons.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.HomeViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.BottomNavBar
import hr.ferit.klarastankovic.innerseasons.ui.components.ScreenTitle
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val todayLog = viewModel.todayLog
    val currentSeason = viewModel.currentSeason
    val currentCycleDay = viewModel.currentCycleDay

    var isPeriod by remember { mutableStateOf(todayLog?.isPeriod ?: false) }
    var mood by remember { mutableStateOf(todayLog?.mood ?: 3) }
    var sleepHours by remember { mutableStateOf(todayLog?.sleepHours ?: 0f) }
    var painLevel by remember { mutableStateOf(todayLog?.painLevel ?: 0) }
    var waterIntake by remember { mutableStateOf(todayLog?.waterIntakeMl ?: 0) }

    LaunchedEffect(todayLog) {
        todayLog?.let {
            isPeriod = it.isPeriod
            mood = it.mood
            sleepHours = it.sleepHours
            painLevel = it.painLevel
            waterIntake = it.waterIntakeMl
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BackgroundWhite,
            ) {
                ScreenTitle(
                    title = "Inner Seasons",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }


        }
    }

    viewModel.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearError() }
                ) {
                    Text("OK")
                }
            }
        )
    }
}