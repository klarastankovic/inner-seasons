package hr.ferit.klarastankovic.innerseasons.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.DayLogViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.MoodSelector
import hr.ferit.klarastankovic.innerseasons.ui.components.PainLevelSlider
import hr.ferit.klarastankovic.innerseasons.ui.components.PeriodToggle
import hr.ferit.klarastankovic.innerseasons.ui.components.ScreenTitle
import hr.ferit.klarastankovic.innerseasons.ui.components.SeasonBadge
import hr.ferit.klarastankovic.innerseasons.ui.components.SleepSlider
import hr.ferit.klarastankovic.innerseasons.ui.components.WaterIntakeCounter
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayLogScreen(
    date: String,
    isEditable: Boolean,
    navController: NavController,
    viewModel: DayLogViewModel
) {
    val cycleDay = viewModel.cycleDay
    val season = viewModel.season
    val seasonDescription = viewModel.seasonDescription
    val errorMessage = viewModel.errorMessage

    val snackbarHostState = remember { SnackbarHostState() }

    val formattedDate = remember(date) {
        try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH) // Thu, 25 Dec 2025
            val parsedDate = input.parse(date)
            output.format(parsedDate ?: date)
        } catch (e: Exception) {
            date
        }
    }

    val isToday = remember(date) { date == LocalDate.now().toString() }

    LaunchedEffect(date) {
        viewModel.loadDataForDate(date)
    }

    LaunchedEffect(errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                Box {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Black
                        )
                    }
                }

                ScreenTitle(
                    title = formattedDate,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    SeasonBadge(
                        season = season,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "- Day $cycleDay of your cycle -",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = seasonDescription,
                        fontSize = 14.sp,
                        color = Black,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(20.dp)
                    )

                    if (!isEditable && !viewModel.hasExistingLog) {
                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = "No other data for this day.",
                            fontSize = 16.sp,
                            color = TextPrimary,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                if (isEditable || viewModel.hasExistingLog) {
                    item {
                        PeriodToggle(
                            isPeriod = viewModel.isPeriod,
                            onPeriodChange = { if (isEditable && isToday) viewModel.updatePeriodStatus(it) },
                            season = viewModel.season,
                            enabled = isEditable && isToday
                        )
                    }

                    item {
                        MoodSelector(
                            selectedMood = viewModel.mood,
                            onMoodSelected = { if (isEditable) viewModel.mood = it },
                            enabled = isEditable
                        )
                    }

                    item {
                        SleepSlider(
                            sleepHours = viewModel.sleepHours,
                            onSleepHoursChange = { if (isEditable) viewModel.sleepHours = it },
                            enabled = isEditable
                        )
                    }

                    item {
                        PainLevelSlider(
                            painLevel = viewModel.painLevel,
                            onPainLevelChange = { if (isEditable) viewModel.painLevel = it },
                            enabled = isEditable
                        )
                    }

                    item {
                        WaterIntakeCounter(
                            waterIntakeMl = viewModel.waterIntake,
                            onWaterIntakeChange = { if (isEditable) viewModel.waterIntake = it },
                            enabled = isEditable
                        )
                    }

                    if (isEditable) {
                        item {
                            Button(
                                onClick = {
                                    viewModel.saveLog(date) {
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryPink,
                                    contentColor = White
                                ),
                                modifier = Modifier
                                    .height(44.dp)
                                    .width(125.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                                enabled = !viewModel.isSaving
                            ) {
                                Text(
                                    text = if (viewModel.isSaving) "Saving..." else "Save log",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}