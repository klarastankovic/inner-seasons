package hr.ferit.klarastankovic.innerseasons.data.model

import java.sql.Timestamp

/**
 * Represents a daily cycle log entry
 * Stores all tracking data for a specific date
 */
data class CycleLog(
    var id: String = "",
    val date: String = "", // Format: "yyyy-mm-dd"
    val isPeriod: Boolean = false,
    val mood: Int = 3, // Scale 1-5: 1=😢, 2=😕, 3=😐, 4=🙂, 5=😄
    val sleepHours: Float = 7f, // Range 0-12 hours
    val painLevel: Int = 0, // Scale 0-10: 0=no pain, 10=severe pain
    val waterIntakeMl: Int = 0, // Water intake in millilitres
    val season: String = Season.WINTER.name,
    val notes: String = "",
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
}