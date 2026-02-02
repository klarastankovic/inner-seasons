package hr.ferit.klarastankovic.innerseasons.data.model

/**
 * Represents a daily cycle log entry
 * Stores all tracking data for a specific date
 */
data class CycleLog(
    var id: String = "",
    var deviceId: String = "",
    val date: String = "", // Format: "yyyy-MM-dd"
    val isPeriod: Boolean = false,
    val mood: Int = 4, // Scale 1-5: 1=😢, 2=😕, 3=😐, 4=🙂, 5=😄
    val sleepHours: Float = 7f, // Range 0-12 hours
    val painLevel: Int = 0, // Scale 0-10: 0=no pain, 10=severe pain
    val waterIntakeMl: Int = 0, // Water intake in millilitres
    val season: String = Season.WINTER.name,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getSeasonEnum(): Season {
        return try {
            Season.valueOf(season)
        } catch (e: Exception) {
            Season.WINTER
        }
    }

    fun getFormattedSleepHours(): String {
        return String.format("%.1f h", sleepHours)
    }

    fun getFormattedWaterIntake(): String {
        return "$waterIntakeMl ml"
    }

    fun getMoodEmoji(): String {
        return when (mood) {
            1 -> "😢"
            2 -> "😕"
            3 -> "😐"
            4 -> "🙂"
            5 -> "😄"
            else -> "🙂"
        }
    }

    fun getFormattedPainLevel(): String {
        return "$painLevel/10"
    }
}