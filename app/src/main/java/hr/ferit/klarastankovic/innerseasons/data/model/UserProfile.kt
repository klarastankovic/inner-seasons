package hr.ferit.klarastankovic.innerseasons.data.model

/**
 * User's cycle profile information
 * Stores cycle configuration and history
 */
data class UserProfile(
    var id: String = "default_user",
    val firstDayOfLastPeriod: String = "", // Format: "yyyy-mm-dd"
    val averageCycleLength: Int = 28, // Average cycle length in days (default 28)
    val averagePeriodLength: Int = 5, // Average period duration in days (default 5)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if profile is properly configured
     */
    fun isConfigured(): Boolean {
        return firstDayOfLastPeriod.isNotEmpty()
    }

    /**
     * Get cycle length options for UI picker
     */
    companion object {
        val CYCLE_LENGHT_RANGE = 21..35
        val PERIOD_LENGHT_RANGE = 3..7

        /**
         * Default values for new users
         */
        fun createDefault(): UserProfile {
            return UserProfile(
                id = "default_user",
                firstDayOfLastPeriod = "",
                averageCycleLength = 28,
                averagePeriodLength = 5
            )
        }
    }
}