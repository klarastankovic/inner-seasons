package hr.ferit.klarastankovic.innerseasons.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.SettingsViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.BottomNavBar
import hr.ferit.klarastankovic.innerseasons.ui.components.OutlinedNumberInputField
import hr.ferit.klarastankovic.innerseasons.ui.components.ScreenTitle
import hr.ferit.klarastankovic.innerseasons.ui.navigation.Routes
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White

@RequiresApi(Build.VERSION_CODES.O)
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
    val showDeleteDialog = remember { mutableStateOf(false) }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Delete all data?") },
            text = { Text("This will permanently remove your entire cycle history and profile. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllLogs {
                            showDeleteDialog.value = false
                            navController.navigate(Routes.ONBOARDING) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    enabled = !viewModel.isDeleting
                ) {
                    Text(if (viewModel.isDeleting) "Deleting..." else "Delete everything", color = PrimaryPink)                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog.value = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.refreshProfile()
    }

    LaunchedEffect(exportSuccess, errorMessage) {
        if (exportSuccess != null) {
            exportMessage?.let { snackbarHostState.showSnackbar(it) }
            viewModel.resetExportStatus()
        }
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                userProfile?.firstDayOfLastPeriod?.let { firstPeriod ->
                    if (firstPeriod.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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

                            Text(
                                text = firstPeriod,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextSecondary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                    Text(
                        text = "${userProfile?.averageCycleLength} days",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                    Text(
                        text = "${userProfile?.averagePeriodLength} days",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // DATA
                Text(
                    text = "Data",
                    fontSize = 20.sp,
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
                        .width(180.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundWhite,
                        contentColor = Black
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = TextPrimary
                    ),
                    enabled = !isExporting
                ) {
                    Text(
                        text = "📥 Export my logs",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showDeleteDialog.value = true
                    },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .height(44.dp)
                        .width(180.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPink,
                        contentColor = White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = PrimaryPink
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                ) {
                    Text(
                        text = "Delete my data",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ABOUT
                Text(
                    text = "About",
                    fontSize = 20.sp,
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
                    fontSize = 16.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "More about Seasons",
                    fontSize = 14.sp,
                    color = PrimaryPink,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate(Routes.SEASONS)
                    }
                )
            }
        }
    }
}