package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.data.repository.CycleRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

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
    var isLoading by mutableStateOf(true)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isLoading = true

            allLogs = repository.getAllLogs()
            userProfile = repository.getUserProfile()

            isLoading = false
        }
    }

    fun setMonth(yearMonth: YearMonth) {
        currentMonth = yearMonth
    }

    fun selectDate(date: LocalDate) {
        selectedDate = date
    }

    fun getSeasonForDate(date: LocalDate): Season {
        return userProfile?.let { profile ->
            CycleCalculator.calculatorSeasonForDate(date, profile)
        } ?: Season.WINTER
    }

    fun getLogForDate(date: LocalDate): CycleLog? {
        return allLogs.find { it.date == date.toString() }
    }
}