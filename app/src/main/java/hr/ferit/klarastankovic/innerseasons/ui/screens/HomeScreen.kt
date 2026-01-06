package hr.ferit.klarastankovic.innerseasons.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.HomeViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.AddLogFAB
import hr.ferit.klarastankovic.innerseasons.ui.components.BottomNavBar
import hr.ferit.klarastankovic.innerseasons.ui.components.ScreenTitle
import hr.ferit.klarastankovic.innerseasons.ui.components.SeasonIndicatorLarge
import hr.ferit.klarastankovic.innerseasons.ui.navigation.Routes
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextPrimary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val userProfile = viewModel.userProfile
    val todayLog = viewModel.todayLog
    val currentSeason = viewModel.currentSeason
    val currentCycleDay = viewModel.currentCycleDay
    val errorMessage = viewModel.errorMessage

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SeasonIndicatorLarge(
                    season = currentSeason,
                    cycleDay = currentCycleDay,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate(Routes.SEASONS)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Seasons are a simple way to describe \n" +
                            "where you are in your cycle.",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Today's log",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Black
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        AddLogFAB(
                            onClick = {
                                navController.navigate(Routes.DAY_LOG) {
                                    popUpTo(Routes.DAY_LOG) { inclusive = true }
                                }
                            },
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) { }
                }
            }
        }
    }
}
