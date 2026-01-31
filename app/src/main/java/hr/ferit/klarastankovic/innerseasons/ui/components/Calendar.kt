package hr.ferit.klarastankovic.innerseasons.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import hr.ferit.klarastankovic.innerseasons.data.model.Season
import hr.ferit.klarastankovic.innerseasons.data.model.UserProfile
import hr.ferit.klarastankovic.innerseasons.data.viewmodel.CalendarViewModel
import hr.ferit.klarastankovic.innerseasons.ui.navigation.Routes
import hr.ferit.klarastankovic.innerseasons.ui.theme.Black
import hr.ferit.klarastankovic.innerseasons.ui.theme.PrimaryPink
import hr.ferit.klarastankovic.innerseasons.ui.theme.TextSecondary
import hr.ferit.klarastankovic.innerseasons.ui.theme.White
import hr.ferit.klarastankovic.innerseasons.utils.CycleCalculator
import hr.ferit.klarastankovic.innerseasons.utils.CycleCalculator.isPeriodDay
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    viewModel: CalendarViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = White
    ) {
        Column(
            modifier = Modifier.padding(30.dp)
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
                profile = viewModel.userProfile,
                currentMonth = viewModel.currentMonth,
                selectedDate = viewModel.selectedDate,
                navController = navController,
                onDateSelected = { date -> viewModel.selectDate(date) },
                getSeasonForDate = { date -> viewModel.getSeasonForDate(date) },
                shouldShowSeasonForDate = { date -> viewModel.shouldShowSeasonForDate(date) },
                isPredictedPeriodStart = { date -> viewModel.isPredictedPeriodStart(date) },
                hasLogForDate = { date -> viewModel.hasLogForDate(date) }
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
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
        modifier = Modifier.fillMaxWidth(),
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
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showMonthDropdown = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
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
                        tint = PrimaryPink
                    )
                }

                DropdownMenu(
                    expanded = showMonthDropdown,
                    onDismissRequest = { showMonthDropdown = false },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(White)
                        .border(1.dp, PrimaryPink, RoundedCornerShape(8.dp))
                ) {
                    (1..12).forEach { month ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = Month.of(month).getDisplayName(
                                        TextStyle.FULL,
                                        Locale.getDefault()
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
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

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showYearDropdown = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
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
                        tint = PrimaryPink
                    )
                }

                DropdownMenu(
                    expanded = showYearDropdown,
                    onDismissRequest = { showYearDropdown = false },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(White)
                        .border(1.dp, PrimaryPink, RoundedCornerShape(8.dp))
                ) {
                    (2020..2050).forEach { year ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = year.toString(),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                           },
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    profile: UserProfile?,
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    navController: NavController,
    onDateSelected: (LocalDate) -> Unit,
    getSeasonForDate: (LocalDate) -> Season,
    shouldShowSeasonForDate: (LocalDate) -> Boolean,
    isPredictedPeriodStart: (LocalDate) -> Boolean,
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
                    color = PrimaryPink,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        val firstDayOfMonth = currentMonth.atDay(1)
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
                                val state = profile?.let {
                                    CycleCalculator.calculateStateForDate(date, it)
                                }
                                val season = if (shouldShowSeasonForDate(date)) {
                                    state?.season
                                } else {
                                    null
                                }
                                val isPredictedStart = isPredictedPeriodStart(date)

                                CalendarDayCell(
                                    date = date,
                                    isSelected = isSelected,
                                    season = season,
                                    cycleDay = state?.cycleDay ?: 0,
                                    isPredictedPeriodStart = isPredictedStart,
                                    isFutureDate = date.isAfter(LocalDate.now()),
                                    hasLog = hasLogForDate(date),
                                    onClick = {
                                        val today = LocalDate.now()
                                        val firstPeriodDate = try {
                                            if (profile?.firstDayOfLastPeriod?.isNotEmpty() == true) {
                                                LocalDate.parse(profile.firstDayOfLastPeriod)
                                            } else {
                                                null
                                            }
                                        } catch (e: Exception) {
                                            null
                                        }

                                        when {
                                            date.isAfter(today) -> return@CalendarDayCell

                                            firstPeriodDate != null && date.isBefore(firstPeriodDate) -> {
                                                navController.navigate(Routes.getNoDataRoute(date.toString()))
                                            }

                                            date == today -> {
                                                onDateSelected(date)
                                                navController.navigate(Routes.getDayLogRoute(date.toString()))
                                            }

                                            hasLogForDate(date) -> {
                                                onDateSelected(date)
                                                navController.navigate(Routes.getDayLogRoute(date.toString()))
                                            }

                                            firstPeriodDate != null /*&& !date.isAfter(today)*/ -> {
                                                navController.navigate(Routes.getViewOnlyRoute(date.toString()))
                                            }

                                            else -> return@CalendarDayCell
                                        }
                                    }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayCell(
    date: LocalDate,
    isSelected: Boolean,
    season: Season?,
    cycleDay: Int,
    isPredictedPeriodStart: Boolean,
    isFutureDate: Boolean,
    hasLog: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected-> PrimaryPink
        cycleDay > 0 -> season?.color ?: Transparent
        else -> White
    }

    val textColor = when {
        isSelected -> White
        else -> Black
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .clip(CircleShape)
            .then(
                when {
                    isSelected -> Modifier.background(PrimaryPink)
                    season != null && cycleDay > 0 -> Modifier.drawBehind {
                        drawCircle(
                            color = bgColor,
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(4f, 4f),
                                    phase = 0f
                                )
                            )
                        )

                        // Only draw dot for PREDICTED period starts (not actual logged periods)
                        if (isPredictedPeriodStart && !hasLog) {
                            drawCircle(
                                color = PrimaryPink,
                                radius = 3.dp.toPx(),
                                center = center
                            )
                        }
                    }
                    isFutureDate -> Modifier
                    else -> Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

sealed class CalendarDay {
    data class Day(val date: LocalDate) : CalendarDay()
    object Empty: CalendarDay()
}