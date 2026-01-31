package hr.ferit.klarastankovic.innerseasons.utils

import android.os.Build
import androidx.annotation.RequiresApi
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class CycleState(
    val season: Season,
    val cycleDay: Int,
    val daysUntilNextSeason: Int
)

@RequiresApi(Build.VERSION_CODES.O)
object CycleCalculator {
    private fun defaultState(): CycleState {
        return CycleState(
            season = Season.WINTER,
            cycleDay = 1,
            daysUntilNextSeason = 5
        )
    }

    private fun calculateCycleDay(daysSinceLastPeriod: Int, cycleLength: Int): Int {
        return (daysSinceLastPeriod % cycleLength) + 1
    }

    fun calculateCurrentState(profile: UserProfile): CycleState {
        val today = LocalDate.now()
        return calculateStateForDate(today, profile)
    }

    fun calculateStateForDate(date: LocalDate, profile: UserProfile): CycleState {
        if (profile.firstDayOfLastPeriod.isEmpty()) {
            return defaultState()
        }

        val lastPeriodDate = LocalDate.parse(profile.firstDayOfLastPeriod)
        val daysBetween = ChronoUnit.DAYS.between(lastPeriodDate, date).toInt()

        val cycleLength = profile.averageCycleLength
        val periodLength = profile.averagePeriodLength

        val cycleDay = calculateCycleDay(
            daysSinceLastPeriod = daysBetween,
            cycleLength = cycleLength
        )

        val season = Season.fromCycleDay(
            daysSinceLastPeriod = daysBetween,
            cycleLength = cycleLength,
            periodLength = periodLength
        )

        val daysUntilNextSeason = calculateDaysUntilNextSeason(cycleDay, cycleLength, periodLength, season)

        return CycleState(
            season = season,
            cycleDay = cycleDay,
            daysUntilNextSeason = daysUntilNextSeason
        )
    }

    fun calculateDaysUntilNextSeason(
        cycleDay: Int,
        cycleLength: Int,
        periodLength: Int,
        season: Season
    ): Int {
        return when (season) {
            Season.WINTER -> {
                (periodLength - cycleDay).coerceAtLeast(1)
            }
            Season.SPRING -> {
                val summerStartDay = (cycleLength * 0.45f).toInt() + 1
                (summerStartDay - cycleDay).coerceAtLeast(1)
            }
            Season.SUMMER -> {
                val autumnStartDay = (cycleLength * 0.60f).toInt() + 1
                (autumnStartDay - cycleDay).coerceAtLeast(1)
            }
            Season.AUTUMN -> {
                // Days until the start of the next cycle (Day 1)
                val daysLeft = cycleLength - cycleDay + 1
                daysLeft.coerceAtLeast(1)
            }
        }
    }

    fun calculateSeasonForDate(date: LocalDate, profile: UserProfile): Season {
        return calculateStateForDate(date, profile).season
    }

    fun predictNextSeason(profile: UserProfile): Pair<Season, LocalDate> {
        val currentState = calculateCurrentState(profile)

        // Get next season (cycles through WINTER → SPRING → SUMMER → AUTUMN → WINTER)
        val entries = Season.entries
        val nextSeason = entries[(currentState.season.ordinal + 1) % entries.size]
        val nextSeasonDate = LocalDate.now().plusDays(currentState.daysUntilNextSeason.toLong())

        return Pair(nextSeason, nextSeasonDate)
    }

    fun isPeriodDay(date: LocalDate, profile: UserProfile): Boolean {
        if (profile.firstDayOfLastPeriod.isEmpty()) return false
        val cycleDay = calculateStateForDate(date, profile).cycleDay
        return cycleDay <= profile.averagePeriodLength
    }

    fun getCurrentMonthDateRange(): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()
        val firstDayOfMonth = today.withDayOfMonth(1)
        val lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth())

        return Pair(firstDayOfMonth, lastDayOfMonth)
    }

    fun formatCycleDay(cycleDay: Int): String {
        return "Day $cycleDay of your cycle"
    }
}