package app.heymoon.calendartest.calendar.model

import java.time.LocalDate

enum class DayOwner {
    PREVIOUS_MONTH,
    THIS_MONTH,
    NEXT_MONTH
}

data class CalendarDay(
    val date: LocalDate,
    val dayOwner: DayOwner
) {
}