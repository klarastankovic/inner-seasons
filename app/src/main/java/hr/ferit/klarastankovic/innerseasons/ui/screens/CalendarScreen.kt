package hr.ferit.klarastankovic.innerseasons.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.CalendarViewModel
import hr.ferit.klarastankovic.innerseasons.ui.components.BottomNavBar
import hr.ferit.klarastankovic.innerseasons.ui.components.ScreenTitle
import hr.ferit.klarastankovic.innerseasons.ui.theme.BackgroundWhite

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    navController: NavController
) {
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
                    title = "Calendar",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }


        }
    }
}