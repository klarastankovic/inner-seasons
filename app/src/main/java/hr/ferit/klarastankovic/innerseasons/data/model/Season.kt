package hr.ferit.klarastankovic.innerseasons.data.model

import androidx.compose.ui.graphics.Color
import hr.ferit.klarastankovic.innerseasons.ui.theme.AutumnOrange
import hr.ferit.klarastankovic.innerseasons.ui.theme.SpringGreen
import hr.ferit.klarastankovic.innerseasons.ui.theme.SummerYellow
import hr.ferit.klarastankovic.innerseasons.ui.theme.WinterBlue

enum class Season(
    val displayName: String,
    val emoji: String,
    val description: String,
    val shortDescription: String,
    val color: Color
) {
    WINTER(
        displayName = "Winter",
        emoji = "❄️",
        description = "Winter is the time of your bleed, when hormones are at their lowest and energy often dips.\nIt is usually a more inward season, ideal for rest, warmth, and doing less where you can.",
        shortDescription = "Winter: inward season, ideal for rest and doing less.",
        color = WinterBlue
    ),
    SPRING(
        displayName = "Spring",
        emoji = "🌸",
        description = "Spring begins as bleeding ends; hormones start to rise and many people feel clearer and more energized.\nIt can be a good season for planning, gentle movement, and starting new things.",
        shortDescription = "Spring: clearer focus, good for planning and starting new things.",
        color = SpringGreen
    ),
    SUMMER(
        displayName = "Summer",
        emoji = "☀️",
        description = "Summer is the ovulation phase, often linked with peak energy, confidence, and sociability.\nThis is a supportive time for visibility, collaboration, and taking on more outward-facing tasks.",
        shortDescription = "Summer: peak energy, supportive time for visibility and collaboration.",
        color = SummerYellow
    ),
    AUTUMN(
        displayName = "Autumn",
        emoji = "🍂",
        description = "Autumn is the pre-menstrual phase, when hormones shift again and you might feel more inward.\nIt's a time for reflection, completion, and honoring what you need emotionally and physically.",
        shortDescription = "Autumn: time for reflection, completion, and honoring your needs.",
        color = AutumnOrange
    );

    companion object {
        /**
         * Determines the season based on day of cycle (1-28+)
         * Uses fixed ranges for typical 28-day cycle:
         * - Winter: days 1-5 (menstruation)
         * - Spring: days 6-13 (follicular)
         * - Summer: days 14-17 (ovulatory)
         * - Autumn: days 18-end (luteal)
         */
        fun fromCycleDay(cycleDay: Int): Season {
            return when (cycleDay) {
                in 1..5 -> WINTER
                in 6..13 -> SPRING
                in 14..17 -> SUMMER
                else -> AUTUMN
            }
        }

        /**
         * Alternative: scale to user's actual cycle length
         */
        fun fromCycleDayScaled(
            daysSinceLastPeriod: Int,
            cycleLength: Int
        ): Season {
            val normalizedDay = (daysSinceLastPeriod * 28 / cycleLength) + 1
            return fromCycleDay(normalizedDay)
        }
    }
}