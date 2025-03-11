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
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Serializable
sealed class HabitDTO {
    abstract val id: String
    abstract val completed: Boolean
    abstract val failed: Boolean

    abstract val enabled: Boolean
    abstract val name: String

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


fun main() {
//    val weekdayHabit: ScheduledHabitPartDTO = WeekdayHabitDTO(
//        "3", "first", true, true, "0", listOf(3)
//    )
//    val intervalHabit: ScheduledHabitPartDTO = IntervalHabitDTO(
//        "3", "first", true, true, "0",
//        SimpleDateDTO(0, 0, 0), interval = 1
//
//    )
//    val dto: HabitDTO = ScheduledHabitDTO(
//        "0", true, true, true, "name",
//        3, listOf(weekdayHabit, intervalHabit)
//    )
//    val habit = dto.toHabit()
//    val dto2 = habit.toHabitDTO()
//    println(habit)
//    println(dto)
//    println(dto2)
//    val str = Json.encodeToString(dto)
//    val str2 = Json.encodeToString(dto2)
//    println(str)
//    println(str2)
    val baby_str1 = """
        {
    "id": "6",
    "completed": false,
    "failed": false,
    "name": "Max 1 Fap",
    "type": "single_habit",
    "start_at_streak": 2,
    "streak_name": { "4": "No Fap" }
  }
    """.trimIndent()
    val baby_str2 = """
        {
    "completed": false,
    "failed": false,
    "type": "scheduled_habit",
    "id": "1",
    "name": "tmp",
    "scheduled_tasks": [
      {
        "completed": false,
        "enabled": true,
        "id": "2808046745",
        "name": "weak",
        "parent": "1",
        "type": "weekdays",
        "weekdays": [0]
      },
      
      {
        "completed": false,
        "enabled": true,
        "id": "3229471855",
        "interval": 1,
        "last_completed_day": {
          "day": 1,
          "month": 1,
          "weekday": 4,
          "year": 1970
        },
        "name": "inter",
        "parent": "1",
        "type": "interval"
      }
    ],
    "start_at_streak": 0
  }
        """.trimIndent()
    val baby_str3 = """
        
        """.trimIndent()
    val string = """
       {
  "6": {
    "id": "6",
    "completed": false,
    "failed": false,
    "name": "Max 1 Fap",
    "type": "single_habit",
    "start_at_streak": 2,
    "streak_name": { "4": "No Fap" }
  },
  "1": {
    "completed": false,
    "failed": false,
    "id": "1",
    "type": "scheduled_habit",
    "name": "tmp",
    "scheduled_tasks": [
      {
        "completed": false,
        "enabled": true,
        "id": "2808046745",
        "name": "weak",
        "parent": "1",
        "type": "weekdays",
        "weekdays": [0]
      },
      
      {
        "completed": false,
        "enabled": true,
        "id": "3229471855",
        "interval": 1,
        "last_completed_day": {
          "day": 1,
          "month": 1,
          "weekday": 4,
          "year": 1970
        },
        "name": "inter",
        "parent": "1",
        "type": "interval"
      }
    ],
    "start_at_streak": 0
  },
  "2": {
    "completed": false,
    "failed": false,
    "id": "2",
    "type": "planned_habit",
    "name": "tmp",
    "planned_task": [
      {
        "date": { "day": 11, "month": 3, "year": 2025 },
        "tasks": ["hello", "world"]
      }
    ],
    "start_at_streak": 0
  }
} 
    """.trimIndent()
    val dto1 = Json.decodeFromString<HabitDTO>(baby_str1)
    println(dto1)
    val dto2 = Json { ignoreUnknownKeys = true }.decodeFromString<HabitDTO>(baby_str2)
    println(dto2)
//    val dto3 = Json { ignoreUnknownKeys = true }.decodeFromString<HabitDTO>(baby_str3)
//    println(dto3)
    val dtos = Json { ignoreUnknownKeys = true }.decodeFromString<Map<String, HabitDTO>>(string)
    dtos.entries.forEach { println(it) }
}