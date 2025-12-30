package hr.ferit.klarastankovic.innerseasons.utils

import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class CycleState(
    val season: Season,
    val cycleDay: Int,
    val daysUntilNextSeason: Int
)

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
        val daysSinceLastPeriod = ChronoUnit.DAYS.between(lastPeriodDate, date).toInt()

        val cycleDay = calculateCycleDay(daysSinceLastPeriod, profile.averageCycleLength)

        val season = Season.fromCycleDayScaled(
            daysSinceLastPeriod = daysSinceLastPeriod,
            cycleLength = profile.averageCycleLength
        )

        val daysUntilNext = calculateDaysUntilNextSeason(cycleDay, profile.averageCycleLength)

        return CycleState(
            season = season,
            cycleDay = cycleDay,
            daysUntilNextSeason = daysUntilNext
        )
    }

    fun calculateDaysUntilNextSeason(cycleDay: Int, cycleLength: Int): Int {
        val normalizedDay = ((cycleDay - 1) * 28 / cycleLength) + 1

        return when (normalizedDay) {
            in 1..5 -> 6 - normalizedDay // Days until Spring
            in 6..13 -> 14 - normalizedDay // Days until Summer
            in 14..17 -> 18 - normalizedDay // Days until Autumn
            else -> { // Days until Winter (next cycle)
                val daysLeftInCycle = cycleLength - cycleDay + 1
                daysLeftInCycle
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