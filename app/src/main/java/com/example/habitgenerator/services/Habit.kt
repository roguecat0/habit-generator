package com.example.habitgenerator.services

data class Habit(
    val id: Int,
    val name: String,
    val enabled: Boolean,
    val startFrom: Int,
    val completed: Boolean,
    val failed: Boolean,
    val habitType: HabitType,
)
sealed interface HabitType {
    data class SingleHabit(
        val streakName: HashMap<Int,String>?,
    )
    data class Scheduled(
        val scheduledHabits: List<ScheduledHabit> = listOf()
    )
    data class Planned(
        val plannedHabits: List<PlannedHabit> = listOf()
    )
}
data class PlannedHabit(
    val name: String,
    val date: SimpleDate,
)
data class ScheduledHabit(
    val id: Int,
    val name: String,
    val completed: Boolean,
    val failed: Boolean,
    val parent: Int,
)
sealed interface ScheduledType{
    data class Weekdays(val activeDays: Array<Boolean> = arrayOf(false,false,false,false,false,false,false))
    data class Interval(val intervalDays: Int, val lastCompletedDate: SimpleDate)
}
data class SimpleDate(
    val day: Int,
    val month: Int,
    val year: Int,
)