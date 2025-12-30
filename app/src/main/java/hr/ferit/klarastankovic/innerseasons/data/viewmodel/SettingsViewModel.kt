package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.data.repository.CycleRepository
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {
    private val repository = CycleRepository()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var exportSuccess by mutableStateOf(null)
        private set

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            userProfile = repository.getUserProfile()
            isLoading = false
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            userProfile = profile
        }
    }

    fun exportDataToCSV(context: Context) {
        viewModelScope.launch {
            isLoading = true

            val logs = repository.getAllLogs()
            val success = CSVExporter.exportToCSV(context, logs)
            exportSuccess = success

            isLoading = false
        }
    }

    fun resetExportStatus() {
        exportSuccess = null
    }
}