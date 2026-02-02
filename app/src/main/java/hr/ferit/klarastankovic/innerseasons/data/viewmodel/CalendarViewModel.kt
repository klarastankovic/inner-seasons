package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.data.repository.CycleRepository
import hr.ferit.klarastankovic.innerseasons.utils.CycleCalculator
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel: ViewModel() {
    private val repository = CycleRepository()

    var allLogs by mutableStateOf<List<CycleLog>>(emptyList())
        private set
    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var currentMonth by mutableStateOf(YearMonth.now())
        private set
    var selectedDate by mutableStateOf(LocalDate.now())
        private set
    var selectedDateLog by mutableStateOf<CycleLog?>(null)
        private set
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

                allLogs = repository.getAllLogs()
                userProfile = repository.getUserProfile()

                updateSelectedDateLog()
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Failed to load data: ${e.message}"
                allLogs = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    private fun updateSelectedDateLog() {
        selectedDateLog = getLogForDate(selectedDate)
    }

    fun getLogForDate(date: LocalDate): CycleLog? {
        return allLogs.find { it.date == date.toString() }
    }

    fun goToPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1)
    }

    fun goToNextMonth() {
        currentMonth = currentMonth.plusMonths(1)
    }

    fun setMonth(yearMonth: YearMonth) {
        currentMonth = yearMonth
    }

    fun selectDate(date: LocalDate) {
        selectedDate = date
        updateSelectedDateLog()
    }

    fun shouldShowSeasonForDate(): Boolean {
        return userProfile?.let { profile ->
            profile.firstDayOfLastPeriod.isNotEmpty()
        } ?: false
    }

    fun isPredictedPeriodStart(date: LocalDate): Boolean {
        return userProfile?.let { profile ->
            if (profile.firstDayOfLastPeriod.isEmpty())
                return false

            val cycleState = CycleCalculator.calculateStateForDate(date, profile)

            val isFutureOrToday = !date.isBefore(LocalDate.now())

            cycleState.cycleDay == 1 && isFutureOrToday && !hasLogForDate(date)
        } ?: false
    }

    fun hasLogForDate(date: LocalDate): Boolean {
        return allLogs.any { it.date == date.toString() }
    }

    fun refreshCalendarData() {
        viewModelScope.launch {
            try {
                isLoading = true

                userProfile = repository.getUserProfile()
                allLogs = repository.getAllLogs()

                val realToday = LocalDate.now()

                if (selectedDate.isBefore(realToday)) {
                    selectedDate = realToday

                    if (currentMonth != YearMonth.from(realToday)) {
                        currentMonth = YearMonth.from(realToday)
                    }
                }

                updateSelectedDateLog()
            } catch (e: Exception) {
                errorMessage = "Failed to refresh: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}