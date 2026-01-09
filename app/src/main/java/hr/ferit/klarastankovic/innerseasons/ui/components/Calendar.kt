package hr.ferit.klarastankovic.innerseasons.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.CalendarViewModel
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Calendar(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CalendarHeader(
                currentMonth = viewModel.currentMonth,
                onPreviousMonth = { viewModel.goToPreviousMonth() },
                onNextMonth = { viewModel.goToNextMonth() },
                onMonthChange = { month ->
                    viewModel.setMonth(YearMonth.of(viewModel.currentMonth.year, month))
                },
                onYearChange = { year ->
                    viewModel.setMonth(YearMonth.of(year, viewModel.currentMonth.monthValue))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CalendarGrid(
                currentMonth = viewModel.currentMonth,
                selectedDate = viewModel.selectedDate,
                onDateSelected = { date -> viewModel.selectDate(date) },
                getSeasonForDate = { date -> viewModel.getSeasonForDate(date) },
                hasLogForDate = { date -> viewModel.hasLogForDate(date) }
            )
        }
    }

}

@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit
) {
    var showMonthDropdown by remember { mutableStateOf(false) }
    var showYearDropdown by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous month",
                tint = Black
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                TextButton(
                    onClick = { showMonthDropdown = true },
                    border = BorderStroke(0.5.dp, TextSecondary)
                ) {
                    Text(
                        text = currentMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        fontSize = 16.sp,
                        color = Black
                    )


                    Spacer(modifier = Modifier.width(2.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "More",
                        tint = Black
                    )
                }

                DropdownMenu(
                    expanded = showMonthDropdown,
                    onDismissRequest = { showMonthDropdown = false }
                ) {
                    (1..12).forEach { month ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    Month.of(month).getDisplayName(
                                        TextStyle.FULL,
                                        Locale.getDefault()
                                    )
                                )
                            },
                            onClick = {
                                onMonthChange(month)
                                showMonthDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            Box {
                TextButton(
                    onClick = { showYearDropdown = true },
                    border = BorderStroke(0.5.dp, TextSecondary)
                ) {
                    Text(
                        text = currentMonth.year.toString(),
                        fontSize = 16.sp,
                        color = Black
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "More",
                        tint = Black
                    )
                }

                DropdownMenu(
                    expanded = showYearDropdown,
                    onDismissRequest = { showYearDropdown = false }
                ) {
                    (2020..2050).forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year.toString()) },
                            onClick = {
                                onYearChange(year)
                                showYearDropdown = false
                            }
                        )
                    }
                }
            }
        }

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next month",
                tint = Black
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    getSeasonForDate: (LocalDate) -> Season,
    hasLogForDate: (LocalDate) -> Boolean
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        val firstDayOfMonth = currentMonth.atDay(1)
        val lastDayOfMonth = currentMonth.atEndOfMonth()
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
        val daysInMonth = currentMonth.lengthOfMonth()

        val calendarDays = mutableListOf<CalendarDay>()

        repeat(firstDayOfWeek) {
            calendarDays.add(CalendarDay.Empty)
        }

        for (day in 1..daysInMonth) {
            calendarDays.add(CalendarDay.Day(currentMonth.atDay(day)))
        }

        calendarDays.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { calendarDay ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when (calendarDay) {
                            is CalendarDay.Day -> {
                                val date = calendarDay.date
                                val isSelected = date == selectedDate
                                val hasLog = hasLogForDate(date)
                                val season = if (hasLog) getSeasonForDate(date) else null

                                CalendarDayCell(
                                    date = date,
                                    isSelected = isSelected,
                                    season = season,
                                    onClick = { onDateSelected(date) }
                                )
                            }

                            CalendarDay.Empty -> {
                                Spacer(modifier = Modifier.size(40.dp))
                            }
                        }
                    }
                }

                repeat(7 - week.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CalendarDayCell(
    date: LocalDate,
    isSelected: Boolean,
    season: Season?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                season?.color?.copy(alpha = 0.3f) ?: White
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if(isSelected) Black else Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            color = Black,
            textAlign = TextAlign.Center
        )
    }
}

sealed class CalendarDay {
    data class Day(val date: LocalDate) : CalendarDay()
    object Empty: CalendarDay()
}