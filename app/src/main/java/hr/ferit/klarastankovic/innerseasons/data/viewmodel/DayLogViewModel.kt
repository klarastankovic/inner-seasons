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
    var cycleDay by mutableIntStateOf(1)
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
    var hasExistingLog by mutableStateOf(false)
        private set
    var isSaving by mutableStateOf(false)
        private set
    var saveSuccess by mutableStateOf<Boolean?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDataForDate(date: String) {
        viewModelScope.launch {
            try {
                resetStateToDefaults()
                userProfile = repository.getUserProfile()
                val localDate = LocalDate.parse(date)

                val existingLog = repository.getLogByDate(date)

                if (existingLog != null) {
                    isPeriod = existingLog.isPeriod
                    mood = existingLog.mood
                    sleepHours = existingLog.sleepHours
                    painLevel = existingLog.painLevel
                    waterIntake = existingLog.waterIntakeMl
                    season = existingLog.getSeasonEnum()
                    seasonDescription = season.shortDescription
                    hasExistingLog = true

                    userProfile?.let { profile ->
                        val state = CycleCalculator.calculateStateForDate(localDate, profile)
                        cycleDay = state.cycleDay
                    }
                } else {
                    userProfile?.let { profile ->
                        val state = CycleCalculator.calculateStateForDate(LocalDate.parse(date), profile)
                        season = state.season
                        seasonDescription = state.season.shortDescription
                        cycleDay = state.cycleDay
                        isPeriod = (season == Season.WINTER)
                    }
                    hasExistingLog = false
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePeriodStatus(isPeriodDay: Boolean, date: String) {
        isPeriod = isPeriodDay

        if (isPeriodDay) {
            season = Season.WINTER
            seasonDescription = Season.WINTER.shortDescription
        } else {
            userProfile?.let { profile ->
                val localDate = LocalDate.parse(date)
                val state = CycleCalculator.calculateStateForDate(localDate, profile)
                season = state.season
                seasonDescription = state.season.shortDescription
            }
            return
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveLog(
        date: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isSaving = true

                val finalSeason = if (isPeriod) Season.WINTER else {
                    userProfile?.let {
                        CycleCalculator.calculateStateForDate(LocalDate.parse(date), it).season
                    } ?: season
                }

                val log = CycleLog(
                    date = date,
                    isPeriod = isPeriod,
                    mood = mood,
                    sleepHours = sleepHours,
                    painLevel = painLevel,
                    waterIntakeMl = waterIntake,
                    season = finalSeason.name,
                    timestamp = System.currentTimeMillis()
                )

                val success = repository.saveLog(log)
                saveSuccess = success

                if (success) onSuccess()
                else errorMessage = "Failed to save log"

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

    private fun resetStateToDefaults() {
        isPeriod = false
        mood = 4
        sleepHours = 7f
        painLevel = 0
        waterIntake = 0
        errorMessage = null
        saveSuccess = null
    }
}