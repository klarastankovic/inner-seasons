package hr.ferit.klarastankovic.innerseasons.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.utils.DeviceIdManager
import kotlinx.coroutines.tasks.await

class CycleRepository {
    private val db = FirebaseFirestore.getInstance()
    private val logsCollection = db.collection("cycle_logs")
    private val profilesCollection = db.collection("user_profiles")

    private fun getDeviceId(): String {
        return DeviceIdManager.getDeviceId()
    }

    // CYCLE LOGS OPERATIONS
    suspend fun getAllLogs(): List<CycleLog> {
        return try {
            val deviceId = getDeviceId()

            val snapshot = logsCollection
                .whereEqualTo("deviceId", deviceId)
                .get()
                .await()

            val logs = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(CycleLog::class.java)?.apply {
                        id = doc.id
                    }
                } catch (e: Exception) {
                    null
                }
            }

            val sortedLogs = logs.sortedByDescending { it.date }

            sortedLogs
        } catch (e: Exception) {
            emptyList()
        }
    }

    // @param date Format: "yyyy-MM-dd"
    suspend fun getLogByDate(date: String): CycleLog? {
        return try {
            val deviceId = getDeviceId()
            val docId = "${deviceId}_${date}"

            val doc = logsCollection
                .document(docId)
                .get()
                .await()

            if (doc.exists()) {
                doc.toObject(CycleLog::class.java)?.apply {
                    id = doc.id
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveLog(log: CycleLog): Boolean {
        return try {
            val deviceId = getDeviceId()
            val specificLogId = "${deviceId}_${log.date}"

            val logToSave = log.copy(
                id = specificLogId,
                deviceId = deviceId
            )

            logsCollection.document(specificLogId)
                .set(logToSave)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllLogs(): Boolean {
        return try {
            val deviceId = getDeviceId()
            val batch = db.batch()

            val logs = logsCollection
                .whereEqualTo("deviceId", deviceId)
                .get()
                .await()

            for (doc in logs) {
                batch.delete(doc.reference)
            }

            batch.commit().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun generateCsvString(): String {
        val deviceId = getDeviceId()

        val snapshot = logsCollection
            .whereEqualTo("deviceId", deviceId)
            .get()
            .await()

        val logs = snapshot.documents.mapNotNull { doc ->
            try {
                doc.toObject(CycleLog::class.java)
            } catch (e: Exception) {
                null
            }
        }

        val sortedLogs = logs.sortedBy { it.date }

        if (sortedLogs.isEmpty()) {
            return "No data found"
        }

        val builder = StringBuilder()
        builder.append("Date,Period,Season,Mood,Sleep,Pain,Water\n")

        sortedLogs.forEach { log ->
            builder.append("${log.date},")
            builder.append("${if(log.isPeriod) "Yes" else "No"},")
            builder.append("${log.season},")
            builder.append("${log.mood},")
            builder.append("${log.sleepHours},")
            builder.append("${log.painLevel},")
            builder.append("${log.waterIntakeMl}\n")
        }
        return builder.toString()
    }


    // USER PROFILE OPERATIONS
    suspend fun getUserProfile(): UserProfile? {
        return try {
            val deviceId = getDeviceId()
            val doc = profilesCollection
                .document(deviceId)
                .get()
                .await()

            if (doc.exists()) {
                doc.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getOrCreateUserProfile(): UserProfile {
        return try {
            val deviceId = getDeviceId()
            val existingProfile = getUserProfile()

            if (existingProfile != null) {
                existingProfile
            } else {
                val newProfile = UserProfile.createDefault(deviceId)
                saveUserProfile(newProfile)
                newProfile
            }
        } catch (e: Exception) {
            UserProfile.createDefault(getDeviceId())
        }
    }

    suspend fun saveUserProfile(profile: UserProfile): Boolean {
        return try {
            val deviceId = getDeviceId()
            val profileToSave = profile.copy(
                id = deviceId,
                deviceId = deviceId,
                isOnboarded = true
            )

            profilesCollection
                .document(deviceId)
                .set(profileToSave)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
