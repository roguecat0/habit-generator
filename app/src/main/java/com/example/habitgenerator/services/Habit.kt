package com.example.habitgenerator.services

data class Habit(
    val id: Int = 0,
    val name: String = "",
    val enabled: Boolean = false,
    val startFrom: Int = 0 ,
    val completed: Boolean = false ,
    val failed: Boolean = false ,
    val habitType: HabitType = HabitType.SingleHabit(),
)
sealed interface HabitType {
    data class SingleHabit(
        val streakName: HashMap<Int,String>? = null,
    ): HabitType
    data class Scheduled(
        val scheduledHabits: List<ScheduledHabit> = listOf()
    ): HabitType
    data class Planned(
        val plannedHabits: List<PlannedHabit> = listOf()
    ): HabitType
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
    data class Weekdays(
        val activeDays: Array<Boolean> =
            arrayOf(false,false,false,false,false,false,false)
    ): ScheduledType
    data class Interval(
        val intervalDays: Int,
        val lastCompletedDate: SimpleDate
    ): ScheduledType
}
data class SimpleDate(
    val day: Int,
    val month: Int,
    val year: Int,
)