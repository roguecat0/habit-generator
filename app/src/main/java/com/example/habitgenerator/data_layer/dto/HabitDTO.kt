package com.example.habitgenerator.data_layer.dto

import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitType
import com.example.habitgenerator.data_layer.ScheduledHabit
import com.example.habitgenerator.data_layer.ScheduledType
import com.example.habitgenerator.data_layer.SimpleDate
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class HabitDTO {
    abstract val id: String
    abstract val completed: Boolean
    abstract val failed: Boolean

    abstract val enabled: Boolean
    abstract val name: String

    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    @SerialName("start_at_streak")
    abstract val startFrom: Int

    abstract fun toHabit(): Habit
}

fun Habit.toHabitDTO(): HabitDTO {
    return when (val type = this.habitType) {
        is HabitType.SingleHabit -> {
            SingleHabitDTO(
                id = id.toString(),
                name = name,
                completed = completed,
                failed = failed,
                enabled = enabled,
                startFrom = if (!enabled) startFrom + 100 else startFrom,
                streakNames = type.streakNames.takeIf { it.isNotEmpty() }?.toMap(),
            )
        }

        is HabitType.Scheduled -> ScheduledHabitDTO(
            id = id.toString(),
            name = name,
            completed = completed,
            failed = failed,
            enabled = enabled,
            startFrom = if (!enabled) startFrom + 100 else startFrom,
            scheduledHabits = type.scheduledHabits.map { it.toScheduledHabitPartDTO() }
        )

        else -> TODO("planned habit type")
    }
}

fun ScheduledHabit.toScheduledHabitPartDTO(): ScheduledHabitPartDTO {
    return when (val type = this.scheduledType) {
        is ScheduledType.Weekdays -> {
            WeekdayHabitDTO(
                id = id.toString(),
                name = name,
                completed = completed,
                enabled = enabled,
                parent = parent.toString(),
                weekdays = type.activeDays.foldIndexed(listOf()) { i, acc, b ->
                    if (b) acc + i else acc
                }
            )

        }

        is ScheduledType.Interval -> {
            IntervalHabitDTO(
                id = id.toString(),
                name = name,
                completed = completed,
                enabled = enabled,
                parent = parent.toString(),
                interval = type.intervalDays,
                lastCompletedDay = SimpleDateDTO(
                    day = type.lastCompletedDate.day,
                    month = type.lastCompletedDate.month,
                    year = type.lastCompletedDate.year,
                )
            )
        }
    }
}

@Serializable
@SerialName("single_habit")
data class SingleHabitDTO(
    override val id: String,
    override val completed: Boolean,
    override val failed: Boolean,
    override val enabled: Boolean,
    override val name: String,
    override val startFrom: Int,
    @SerialName("streak_name")
    val streakNames: Map<Int, String>? = null,
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
        habitType = HabitType.SingleHabit(
            streakNames = streakNames?.toList() ?: listOf()
        )
    )
}

@Serializable
@SerialName("scheduled_habit")
data class ScheduledHabitDTO(
    override val id: String,
    override val completed: Boolean,
    override val failed: Boolean,
    override val enabled: Boolean,
    override val name: String,
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
            id = this.id.toInt(),
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
data class SimpleDateDTO(
    val year: Int,
    val month: Int,
    val day: Int,
) {
    fun toSimpleDate(): SimpleDate = SimpleDate(
        day = day,
        month = month,
        year = year,
    )
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
            id = id.toInt(),
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


fun main() {
    val weekdayHabit: ScheduledHabitPartDTO = WeekdayHabitDTO(
        "3", "first", true, true, "0", listOf(3)
    )
    val intervalHabit: ScheduledHabitPartDTO = IntervalHabitDTO(
        "3", "first", true, true, "0",
        SimpleDateDTO(0, 0, 0), interval = 1

    )
    val dto: HabitDTO = ScheduledHabitDTO(
        "0", true, true, true, "name",
        3, listOf(weekdayHabit, intervalHabit)
    )
    val habit = dto.toHabit()
    val dto2 = habit.toHabitDTO()
    println(habit)
    println(dto)
    println(dto2)
    val str = Json.encodeToString(dto)
    val str2 = Json.encodeToString(dto2)
    println(str)
    println(str2)
}