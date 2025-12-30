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

class HomeViewModel: ViewModel() {
    private val repository = CycleRepository()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var todayLog by mutableStateOf<CycleLog?>(null)
        private set
    var currentSeason by mutableStateOf(Season.WINTER)
        private set
    var currentCycleDay by mutableStateOf(1)
        private set
    var isLoading by mutableStateOf(true)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            isLoading = true

            userProfile = repository.getUserProfile()

            val today = LocalDate.now().toString()
            todayLog = repository.getLogByDate(today)

            userProfile?.let { profile ->
                val calculation = CycleCalculator.calculateCurrentState(profile)
                currentSeason = calculation.season
                currentCycleDay = calculation.cycleDay
            }

            isLoading = false
        }
    }

    fun saveTodayLog(log: CycleLog) {
        viewModelScope.launch {
            repository.saveLog(log)
            todayLog = log
        }
    }

    fun refreshData() {
        loadData()
    }
}