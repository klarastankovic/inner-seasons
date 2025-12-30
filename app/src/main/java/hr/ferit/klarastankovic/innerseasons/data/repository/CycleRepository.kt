package hr.ferit.klarastankovic.innerseasons.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import hr.ferit.klarastankovic.innerseasons.data.model.CycleLog
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import kotlinx.coroutines.tasks.await

/**
 * Repository for managing cycle data in Firebase
 * Handles all database operations for logs and user profile
 */
class CycleRepository {
    private val db = FirebaseFirestore.getInstance()
    private val logsCollection = db.collection("cycle_logs")
    private val profilesCollection = db.collection("user_profiles")


    // CYCLE LOGS OPERATIONS

    suspend fun getAllLogs(): List<CycleLog> {
        return try {
            logsCollection
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

    // @param date Format: "yyyy-mm-dd"
    suspend fun getLogByDate(date: String): CycleLog? {
        return try {
            val snapshot = logsCollection
                .whereEqualTo("date", date)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(CycleLog::class.java)?.apply {
                    id = doc.id
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    // @param startDate Format: "yyyy-mm-dd"
    // @param endDate Format: "yyyy-mm-dd"
    suspend fun getLogsInRange(startDate: String, endDate: String): List<CycleLog> {
        return try {
            logsCollection
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
            if (log.id.isEmpty()) {
                logsCollection.add(log).await()
            } else {
                logsCollection.document(log.id).set(log).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteLog(logId: String): Boolean {
        return try {
            logsCollection.document(logId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteAllLogs(): Boolean {
        return try {
            val logs = getAllLogs()
            logs.forEach { log ->
                logsCollection.document(log.id).delete().await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }


    // USER PROFILE OPERATIONS

    suspend fun getUserProfile(): UserProfile? {
        return try {
            val doc = profilesCollection.document("default_user").get().await()
            if (doc.exists()) {
                doc.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e:Exception) {
            null
        }
    }

    suspend fun saveUserProfile(profile: UserProfile): Boolean {
        return try {
            profilesCollection
                .document("default_user")
                .set(profile.copy(updatedAt = System.currentTimeMillis()))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun profileExists(): Boolean {
        return try {
            profilesCollection
                .document("default_user")
                .get()
                .await()
                .exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteUserProfile(): Boolean {
        return try {
            profilesCollection
                .document("default_user")
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}