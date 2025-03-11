package com.example.habitgenerator.data_layer.dto

import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitType
import com.example.habitgenerator.data_layer.ScheduledHabit
import com.example.habitgenerator.data_layer.ScheduledType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.random.Random

@Serializable
@SerialName("scheduled_habit")
data class ScheduledHabitDTO(
    override val id: String,
    override val completed: Boolean,
    override val failed: Boolean,
    @Transient
    override val enabled: Boolean = true,
    override val name: String,
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    @SerialName("start_at_streak")
    override val startFrom: Int,
    @SerialName("scheduled_tasks")
    val scheduledHabits: List<ScheduledHabitPartDTO>
) : HabitDTO() {
    override fun toHabit(): Habit = Habit(
        id = id.toInt(),
        name = name,
        completed = completed,
        failed = failed,
        // dirty quick fix
        // todo: make normal better enabled functionality in other app
        enabled = if (startFrom >= 100) false else enabled,
        startFrom = if (startFrom >= 100) startFrom - 100 else startFrom,
        habitType = HabitType.Scheduled(
            scheduledHabits = scheduledHabits.map { it.toScheduledHabit() }
        )
    )
}

@Serializable
sealed class ScheduledHabitPartDTO {
    abstract val id: String
    abstract val name: String
    abstract val completed: Boolean
    abstract val enabled: Boolean
    abstract val parent: String

    abstract fun toScheduledHabit(): ScheduledHabit
}

@Serializable
@SerialName("weekdays")
data class WeekdayHabitDTO(
    override val id: String,
    override val name: String,
    override val completed: Boolean,
    override val enabled: Boolean,
    override val parent: String,
    val weekdays: List<Int>,
) : ScheduledHabitPartDTO() {
    override fun toScheduledHabit(): ScheduledHabit {
        return ScheduledHabit(
            id = this.id.toIntOrNull() ?: Random.nextInt(),
            name = this.name,
            completed = this.completed,
            enabled = this.enabled,
            parent = this.parent.toInt(),
            scheduledType = ScheduledType.Weekdays(
                activeDays = parseActiveDays()
            )
        )
    }

    private fun parseActiveDays(): List<Boolean> {
        return (0..6).map { i -> this.weekdays.contains(i) }
    }
}


@Serializable
@SerialName("interval")
data class IntervalHabitDTO(
    override val id: String,
    override val name: String,
    override val completed: Boolean,
    override val enabled: Boolean,
    override val parent: String,
    @SerialName("last_completed_day")
    val lastCompletedDay: SimpleDateDTO,
    val interval: Int
) : ScheduledHabitPartDTO() {


    override fun toScheduledHabit(): ScheduledHabit {
        return ScheduledHabit(
            id = id.toIntOrNull() ?: Random.nextInt(),
            name = name,
            completed = completed,
            enabled = enabled,
            parent = parent.toInt(),
            scheduledType = ScheduledType.Interval(
                intervalDays = interval,
                lastCompletedDate = lastCompletedDay.toSimpleDate()
            )
        )
    }

}
