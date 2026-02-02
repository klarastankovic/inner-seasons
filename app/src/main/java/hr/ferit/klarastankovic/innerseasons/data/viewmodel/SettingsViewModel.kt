package hr.ferit.klarastankovic.innerseasons.data.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
    var isDeleting by mutableStateOf(false)
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
                    userProfile = repository.getOrCreateUserProfile()
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load profile: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProfileWithFirstPeriod(startDate: LocalDate, cycleLength: Int, periodLength: Int) {
        viewModelScope.launch {
            try {
                isSaving = true
                val currentProfile = userProfile ?: UserProfile()
                val updatedProfile = currentProfile.copy(
                    firstDayOfLastPeriod = startDate.toString(),
                    averageCycleLength = cycleLength,
                    averagePeriodLength = periodLength,
                    isOnboarded = true
                )

                val success = repository.saveUserProfile(updatedProfile)
                if (success) {
                    userProfile = updatedProfile
                } else {
                    errorMessage = "Failed to save your settings."
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isSaving = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

                val csvString = repository.generateCsvString()
                if (csvString == "No data found") {
                    exportSuccess = false
                    exportMessage = "No data to export"
                    return@launch
                }

                val success = CSVExporter.exportToCSV(context, csvString)
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
        return length in UserProfile.CYCLE_LENGTH_RANGE
    }

    fun isValidPeriodLength(length: Int): Boolean {
        return length in UserProfile.PERIOD_LENGTH_RANGE
    }

    fun deleteAllLogs(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                isDeleting = true
                val success = repository.deleteAllLogs()
                if (success) {
                    onComplete()
                } else {
                    errorMessage = "Failed to delete data"
                }
            } catch (e: Exception) {
                errorMessage = "Error deleting data: ${e.message}"
            } finally {
                isDeleting = false
            }
        }
    }

    fun refreshProfile() {
        loadProfile()
    }
}