package hr.ferit.klarastankovic.innerseasons.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.SettingsViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.BottomNavBar
import hr.ferit.klarastankovic.innerseasons.ui.components.DateInputField
import hr.ferit.klarastankovic.innerseasons.ui.components.ScreenTitle
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val userProfile = viewModel.userProfile
    val isExporting = viewModel.isExporting
    val exportSuccess = viewModel.exportSuccess
    val exportMessage = viewModel.exportMessage
    val errorMessage = viewModel.errorMessage

    val snackbarHostState = remember { SnackbarHostState() }

    val selectedDate = remember { mutableStateOf(userProfile?.firstDayOfLastPeriod ?: "") }
    val cycleLengthInput = remember { mutableStateOf(userProfile?.averageCycleLength?.toString() ?: "28") }
    val periodLengthInput = remember { mutableStateOf(userProfile?.averagePeriodLength?.toString() ?: "5") }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            selectedDate.value = it.firstDayOfLastPeriod
            cycleLengthInput.value = it.averageCycleLength.toString()
            periodLengthInput.value = it.averagePeriodLength.toString()
        }
    }

    LaunchedEffect(exportSuccess) {
        if (exportSuccess == true) {
            exportMessage?.let {
                snackbarHostState.showSnackbar(it)
            }
            viewModel.resetExportStatus()
        } else if (exportSuccess == false) {
            errorMessage?.let {
                snackbarHostState.showSnackbar(it)
            }
            viewModel.resetExportStatus()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        },
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
                color = BackgroundWhite
            ) {
                ScreenTitle(
                    title = "Settings",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                // MY CYCLE
                Text(
                    text = "My cycle",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "First day of last period:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    DateInputField(
                        value = selectedDate.value,
                        onDateSelected = { newDate ->
                            selectedDate.value = newDate
                        },
                        context = context,
                        modifier = Modifier.width(144.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // DATA
                Text(
                    text = "Data",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.exportDataToCSV(context)
                    },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .height(44.dp)
                        .width(174.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundWhite,
                        contentColor = Black
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = TextPrimary
                    ),
                    enabled = !isExporting
                ) {
                    Text(
                        text = "📥 Export my logs",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ABOUT
                Text(
                    text = "About",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Inspired by Maisie Hill's ")
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append("Period Power")
                        }
                        append(".")
                    },
                    fontSize = 18.sp,
                    color = TextPrimary
                )
            }
        }
    }
}