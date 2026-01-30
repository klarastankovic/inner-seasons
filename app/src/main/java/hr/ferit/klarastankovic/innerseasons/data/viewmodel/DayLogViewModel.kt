package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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

class DayLogViewModel : ViewModel() {
    private val repository = CycleRepository()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var cycleDay by mutableStateOf(1)
        private set
    var season by mutableStateOf(Season.WINTER)
        private set
    var seasonDescription by mutableStateOf(Season.WINTER.shortDescription)
        private set
    var isPeriod by mutableStateOf(false)
    var mood by mutableIntStateOf(4)
    var sleepHours by mutableFloatStateOf(7f)
    var painLevel by mutableIntStateOf(0)
    var waterIntake by mutableIntStateOf(0)
    var isSaving by mutableStateOf(false)
        private set
    var saveSuccess by mutableStateOf<Boolean?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    fun updatePeriodStatus(isPeriodDay: Boolean) {
        isPeriod = isPeriodDay

        if (isPeriodDay && season != Season.WINTER) {
            season = Season.WINTER
            seasonDescription = Season.WINTER.shortDescription
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDataForDate(date: String) {
        viewModelScope.launch {
            try {
                resetStateToDefaults()

                userProfile = repository.getUserProfile()
                userProfile?.let { profile ->
                    val localDate = LocalDate.parse(date)
                    val state = CycleCalculator.calculateStateForDate(localDate, profile)
                    cycleDay = state.cycleDay
                    season = state.season
                    seasonDescription = season.shortDescription

                    if (season == Season.WINTER) {
                        isPeriod = true
                    }

                    val existingLog = repository.getLogByDate(date)
                    existingLog?.let { log ->
                        isPeriod = log.isPeriod
                        mood = log.mood
                        sleepHours = log.sleepHours
                        painLevel = log.painLevel
                        waterIntake = log.waterIntakeMl

                        season = log.getSeasonEnum()
                        seasonDescription = season.shortDescription
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
            }
        }
    }

    fun saveLog(
        date: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isSaving = true

                val finalSeason = if (isPeriod) Season.WINTER else season

                val log = CycleLog(
                    date = date,
                    isPeriod = isPeriod,
                    mood = mood,
                    sleepHours = sleepHours,
                    painLevel = painLevel,
                    waterIntakeMl = waterIntake,
                    season = season.name,
                    timestamp = System.currentTimeMillis()
                )

                val success = repository.saveLog(log)
                saveSuccess = success

                if (success) {
                    onSuccess()
                } else {
                    errorMessage = "Failed to save log"
                }
            } catch (e: Exception) {
                saveSuccess = false
                errorMessage = "Error saving log: ${e.message}"
            } finally {
                isSaving = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun resetSaveStatus() {
        saveSuccess = null
    }

    private fun resetStateToDefaults() {
        isPeriod = false
        mood = 4
        sleepHours = 7f
        painLevel = 0
        waterIntake = 0
    }
}