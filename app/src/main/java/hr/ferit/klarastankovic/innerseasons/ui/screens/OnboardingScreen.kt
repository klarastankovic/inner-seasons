package hr.ferit.klarastankovic.innerseasons.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.SettingsViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.DateInputField
import hr.ferit.klarastankovic.innerseasons.ui.components.OutlinedNumberInputField
import hr.ferit.klarastankovic.innerseasons.ui.navigation.Routes
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf("") }  // ← EMPTY for new user
    val cycleLengthInput = remember { mutableStateOf("28") }
    val periodLengthInput = remember { mutableStateOf("5") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Inner Seasons!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Let's set up your cycle to get started",
                fontSize = 16.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // First day of last period
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "First day of last period:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black,
                    modifier = Modifier.weight(1f)
                )

                DateInputField(
                    value = selectedDate.value,
                    onDateSelected = { newDate -> selectedDate.value = newDate },
                    context = context,
                    modifier = Modifier.width(140.dp)
                )
            }

            // Average cycle length
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Average cycle length:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black,
                    modifier = Modifier.weight(1f)
                )

                OutlinedNumberInputField(
                    value = cycleLengthInput.value,
                    onValueChange = { cycleLengthInput.value = it },
                    placeholder = "21-35",
                    modifier = Modifier.width(75.dp)
                )
            }

            // Average period length
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Average period length:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black,
                    modifier = Modifier.weight(1f)
                )

                OutlinedNumberInputField(
                    value = periodLengthInput.value,
                    onValueChange = { periodLengthInput.value = it },
                    placeholder = "3-7",
                    modifier = Modifier.width(75.dp)
                )
            }

            Button(
                onClick = {
                    val parsedDate = try {
                        val parts = selectedDate.value.split("/")
                        if (parts.size == 3) {
                            LocalDate.of(
                                parts[2].toInt(),
                                parts[1].toInt(),
                                parts[0].toInt()
                            )
                        } else null
                    } catch (e: Exception) {
                        null
                    }

                    val cycleLength = cycleLengthInput.value.toIntOrNull() ?: 28
                    val periodLength = periodLengthInput.value.toIntOrNull() ?: 5

                    when {
                        parsedDate == null -> {
                            viewModel.errorMessage = "Please select a valid date"
                        }
                        !viewModel.isValidCycleLength(cycleLength) -> {
                            viewModel.errorMessage = "Cycle length must be between 21-35 days"
                        }
                        !viewModel.isValidPeriodLength(periodLength) -> {
                            viewModel.errorMessage = "Period length must be between 3-7 days"
                        }
                        else -> {
                            viewModel.updateProfile(parsedDate, cycleLength, periodLength)
                            // ← Navigate to HOME after successful setup
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .height(44.dp)
                    .width(150.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPink,
                    contentColor = White
                ),
                enabled = !viewModel.isSaving
            ) {
                Text(
                    text = if (viewModel.isSaving) "Setting up..." else "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}