package hr.ferit.klarastankovic.innerseasons.data.model

import java.util.UUID

/**
 * User's cycle profile information
 * Stores cycle configuration and history
 */
data class UserProfile(
    var id: String = "",
    var deviceId: String = "",
    val firstDayOfLastPeriod: String = "", // Format: "yyyy-MM-dd"
    val averageCycleLength: Int = 28, // Average cycle length in days (default 28)
    val averagePeriodLength: Int = 5, // Average period duration in days (default 5)
    var installId: String = "",
    var isOnboarded: Boolean = false
) {
    fun isConfigured(): Boolean {
        return firstDayOfLastPeriod.isNotEmpty()  && isOnboarded
    }

    companion object {
        val CYCLE_LENGTH_RANGE = 21..35
        val PERIOD_LENGTH_RANGE = 3..7

        fun createDefault(deviceId: String): UserProfile {
            return UserProfile(
                id = deviceId,
                deviceId = deviceId,
                firstDayOfLastPeriod = "",
                averageCycleLength = 28,
                averagePeriodLength = 5,
                installId = generateInstallId()
            )
        }

        fun generateInstallId(): String {
            return UUID.randomUUID().toString()
        }
    }
}