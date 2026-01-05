package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.data.repository.CycleRepository
import hr.ferit.klarastankovic.innerseasons.utils.CSVExporter
import kotlinx.coroutines.launch
import java.time.LocalDate

class SettingsViewModel: ViewModel() {
    private val repository = CycleRepository()

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isSaving by mutableStateOf(false)
        private set
    var isExporting by mutableStateOf(false)
        private set
    var exportSuccess by mutableStateOf<Boolean?>(null)
        private set
    var exportMessage by mutableStateOf<String?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                isLoading = true
                userProfile = repository.getUserProfile()

                if (userProfile == null) {
                    userProfile = UserProfile.createDefault()
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load profile: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfile(
        firstDayOfLastPeriod: LocalDate?,
        averageCycleLength: Int,
        averagePeriodLength: Int
    ) {
        viewModelScope.launch {
            try {
                isSaving = true
                errorMessage = null

                val updatedProfile = UserProfile(
                    id = "default_user",
                    firstDayOfLastPeriod = firstDayOfLastPeriod?.toString() ?: "",
                    averageCycleLength = averageCycleLength,
                    averagePeriodLength = averagePeriodLength,
                    createdAt = userProfile?.createdAt ?: System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                val success = repository.saveUserProfile(updatedProfile)
                if (success) {
                    userProfile = updatedProfile
                } else {
                    errorMessage = "Failed to save profile"
                }
            } catch (e: Exception) {
                errorMessage = "Error saving profile: ${e.message}"
            } finally {
                isSaving = false
            }
        }
    }

    fun exportDataToCSV(context: Context) {
        viewModelScope.launch {
            try {
                isExporting = true
                exportSuccess = null
                exportMessage = null

                if (!CSVExporter.isExternalStorageWritable()) {
                    exportSuccess = false
                    exportMessage = "External storage not available"
                    return@launch
                }

                val logs = repository.getAllLogs()
                if (logs.isEmpty()) {
                    exportSuccess = false
                    exportMessage = "No data to export"
                    return@launch
                }

                val success = CSVExporter.exportToCSV(context, logs)
                exportSuccess = success

                if (success) {
                    exportMessage = "Data file exported to Downloads"
                } else {
                    exportMessage = "Failed to export data"
                }

            } catch (e: Exception) {
                exportSuccess = false
                exportMessage = "Export error: ${e.message}"
            } finally {
                isExporting = false
            }
        }
    }

    fun resetExportStatus() {
        exportSuccess = null
        exportMessage = null
    }

    fun clearError() {
        errorMessage = null
    }

    fun isValidCycleLength(length: Int): Boolean {
        return length in UserProfile.CYCLE_LENGHT_RANGE
    }

    fun isValidPeriodLength(length: Int): Boolean {
        return length in UserProfile.PERIOD_LENGHT_RANGE
    }
}