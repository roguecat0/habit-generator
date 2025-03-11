package com.example.habitgenerator.data_layer

data class Habit(
    val id: Int = 0,
    val name: String = "",
    val enabled: Boolean = true,
    val startFrom: Int = 0,
    val completed: Boolean = false,
    val failed: Boolean = false,
    val habitType: HabitType = HabitType.SingleHabit(),
)

sealed interface HabitType {
    data class SingleHabit(
        val streakNames: List<Pair<Int, String>> = listOf(),
    ) : HabitType

    data class Scheduled(
        val scheduledHabits: List<ScheduledHabit> = listOf()
    ) : HabitType

    data class Planned(
        val plannedHabits: List<PlannedHabit> = listOf()
    ) : HabitType
}

data class PlannedHabit(
    val name: String,
    val date: SimpleDate,
)

data class ScheduledHabit(
    val id: Int = 0,
    val name: String = "",
    val completed: Boolean = true,
    val enabled: Boolean = true,
    val parent: Int = -1,
    val scheduledType: ScheduledType,
)

sealed interface ScheduledType {
    data class Weekdays(
        val activeDays: List<Boolean> =
            listOf(false, false, false, false, false, false, false)
    ) : ScheduledType

    data class Interval(
        val intervalDays: Int = 0,
        val lastCompletedDate: SimpleDate = SimpleDate()
    ) : ScheduledType
}

data class SimpleDate(
    val day: Int = 1,
    val month: Int = 1,
    val year: Int = 2000,
)
