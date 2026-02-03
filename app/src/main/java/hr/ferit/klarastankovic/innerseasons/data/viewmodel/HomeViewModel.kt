package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.data.repository.CycleRepository
import hr.ferit.klarastankovic.innerseasons.utils.CycleCalculator
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel: ViewModel() {
    private val repository = CycleRepository()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var todayLog by mutableStateOf<CycleLog?>(null)
        private set
    var currentSeason by mutableStateOf(Season.WINTER)
        private set
    var currentCycleDay by mutableIntStateOf(1)
        private set
    var daysUntilNextSeason by mutableIntStateOf(0)
        private set
    var nextSeason by mutableStateOf<Pair<Season, LocalDate>?>(null)
    var isLoading by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                userProfile = repository.getUserProfile()

                val today = LocalDate.now().toString()
                todayLog = repository.getLogByDate(today)

                userProfile?.let { profile ->
                    val cycleState = CycleCalculator.calculateCurrentState(profile)
                    currentSeason = cycleState.season
                    currentCycleDay = cycleState.cycleDay
                    daysUntilNextSeason = cycleState.daysUntilNextSeason
                    nextSeason = CycleCalculator.predictNextSeason(profile)
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateWaterIntake(amount: Int) {
        viewModelScope.launch {
            val todayDate = LocalDate.now().toString()
            val currentLog = repository.getLogByDate(todayDate)
                ?: CycleLog(date = todayDate, deviceId = "")

            val newWaterLevel = currentLog.waterIntakeMl + amount

            val updatedLog = currentLog.copy(
                waterIntakeMl = newWaterLevel,
                recordedAt = System.currentTimeMillis()
            )

            repository.saveLog(updatedLog)

            todayLog = updatedLog
        }
    }

    fun refreshData() {
        loadData()
    }

    fun clearError() {
        errorMessage = null
    }
}