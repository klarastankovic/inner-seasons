package hr.ferit.klarastankovic.innerseasons.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.utils.DeviceIdManager
import kotlinx.coroutines.tasks.await

/**
 * Repository for managing cycle data in Firebase
 * Handles all database operations for logs and user profile
 */
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
            logsCollection
                .whereEqualTo("deviceId", deviceId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    doc.toObject(CycleLog::class.java)?.apply {
                        id = doc.id
                    }
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // @param date Format: "yyyy-MM-dd"
    suspend fun getLogByDate(date: String): CycleLog? {
        return try {
            val deviceId = getDeviceId()
            val docId = "${deviceId}_${date}"

            val doc = logsCollection.document(docId).get().await()

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

    // @param startDate Format: "yyyy-MM-dd"
    // @param endDate Format: "yyyy-MM-dd"
    suspend fun getLogsInRange(startDate: String, endDate: String): List<CycleLog> {
        return try {
            val deviceId = getDeviceId()
            logsCollection
                .whereEqualTo("deviceId", deviceId)
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    doc.toObject(CycleLog::class.java)?.apply {
                        id = doc.id
                    }
                }
        } catch (e: Exception) {
            emptyList()
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

            logsCollection.document(specificLogId).set(logToSave).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteLog(logId: String): Boolean {
        return try {
            val deviceId = getDeviceId()
            val log = logsCollection.document(logId).get().await()

            if (log.getString("deviceId") != deviceId) {
                return false  // Not device owner
            }

            logsCollection.document(logId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteAllLogs(): Boolean {
        return try {
            val deviceId = getDeviceId()
            val batch = db.batch()

            val logs = logsCollection.whereEqualTo("deviceId", deviceId).get().await()
            for (doc in logs) {
                batch.delete(doc.reference)
            }

            val profileRef = profilesCollection.document(deviceId)
            batch.delete(profileRef)

            batch.commit().await()

            DeviceIdManager.clearDeviceId()
            true
        } catch (e: Exception) {
            false
        }
    }


    // USER PROFILE OPERATIONS

    suspend fun getUserProfile(): UserProfile? {
        return try {
            val deviceId = getDeviceId()
            val doc = profilesCollection.document(deviceId).get().await()

            if (doc.exists()) {
                doc.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e:Exception) {
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
                updatedAt = System.currentTimeMillis(),
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

    suspend fun profileExists(): Boolean {
        return try {
            val deviceId = getDeviceId()
            profilesCollection
                .document(deviceId)
                .get()
                .await()
                .exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteUserProfile(): Boolean {
        return try {
            val deviceId = getDeviceId()
            profilesCollection
                .document(deviceId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}