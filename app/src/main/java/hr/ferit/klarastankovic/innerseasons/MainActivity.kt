package hr.ferit.klarastankovic.innerseasons

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hr.ferit.klarastankovic.innerseasons.ui.navigation.AppNavigation
import hr.ferit.klarastankovic.innerseasons.ui.theme.InnerSeasonsTheme
import hr.ferit.klarastankovic.innerseasons.utils.DeviceIdManager

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        DeviceIdManager.initialize(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InnerSeasonsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}