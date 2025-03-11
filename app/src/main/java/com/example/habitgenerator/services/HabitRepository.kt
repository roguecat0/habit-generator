package com.example.habitgenerator.services

import com.example.habitgenerator.services.dto.SingleHabitDTO2
import com.example.habitgenerator.services.dto.toDTO
import com.example.habitgenerator.services.dto.toTamaCompatString
import com.example.habitgenerator.services.dto.toTamaCompatStringWithSpecials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

const val TAG = "HabitRepository"

class HabitRepository {
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> get() = _habits
    fun changeHabitName(habit: Habit, name: String): Habit {
        return habit.copy(name = name)
    }

    fun deleteHabit(id: Int) {
        _habits.value = _habits.value.filterIndexed { i, _ -> i != id }
    }

    fun getHabits2(): StateFlow<List<Habit>> = habits

    fun changeAHabitValue(id: Int, operation: (Habit) -> Habit) {
        _habits.value = mapHabitAtId(_habits.value, id, operation)
    }

    fun addNewHabit() {
        val id = getNewId(_habits.value)
        _habits.value += Habit(id = id)
    }

    fun addScheduledWeek(habit: Habit): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits + ScheduledHabit(
                    scheduledType = ScheduledType.Weekdays()
                )
            )
        }
    }

    fun addScheduledInterval(habit: Habit): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits + ScheduledHabit(
                    scheduledType = ScheduledType.Interval()
                )
            )
        }
    }

    fun toggleScheduledHabitEnabled(habit: Habit, index: Int): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits.mapIndexed { i, scheduledHabit ->
                    if (i == index) {
                        scheduledHabit.copy(enabled = !scheduledHabit.enabled)
                    } else {
                        scheduledHabit
                    }
                }
            )
        }
    }

    fun changeIntervalAmount(habit: Habit, scheduledIndex: Int, interval: String): Habit {
        val amount = interval.toIntOrNull() ?: 0
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits.mapIndexed { i, scheduledHabit ->
                    scheduledHabit.takeIf { i != scheduledIndex } ?: scheduledHabit.copy(
                        scheduledType = when (val type = scheduledHabit.scheduledType) {
                            is ScheduledType.Interval -> type.copy(intervalDays = amount)
                            else -> type
                        }
                    )
                }
            )
        }
    }

    fun toggleWeekdayEnabled(habit: Habit, scheduledIndex: Int, weekdayIndex: Int): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits.mapIndexed { i, scheduledHabit ->
                    scheduledHabit.takeIf { i != scheduledIndex } ?: scheduledHabit.copy(
                        scheduledType = when (val type = scheduledHabit.scheduledType) {
                            is ScheduledType.Weekdays -> {
                                type.copy(activeDays = type.activeDays.mapIndexed { i, bool ->
                                    bool.takeIf { i != weekdayIndex } ?: !bool
                                })

                            }

                            else -> type
                        }

                    )
                }
            )
        }
    }

    fun changeScheduledHabitName(habit: Habit, index: Int, name: String): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits.mapIndexed { i, scheduledHabit ->
                    scheduledHabit.takeIf { i != index } ?: scheduledHabit.copy(
                        name = name
                    )
                }
            )
        }
    }

    fun deleteScheduledHabit(habit: Habit, index: Int): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits.filterIndexed { i, _ ->
                    i != index
                }
            )
        }
    }

    fun changeScheduleValue(
        habit: Habit,
        operation: (HabitType.Scheduled) -> HabitType.Scheduled
    ): Habit {
        return habit.copy(
            habitType = when (val type = habit.habitType) {
                is HabitType.Scheduled -> operation(type)
                else -> type
            }
        )
    }


    fun addStreakName(habit: Habit): Habit {
        return when (val type = habit.habitType) {
            is HabitType.SingleHabit -> {
                habit.copy(
                    habitType = HabitType.SingleHabit(
                        streakNames = type.streakNames + (0 to "")
                    )
                )
            }

            else -> habit
        }
    }

    fun rotateType(habit: Habit): Habit {
        return habit.copy(
            habitType = when (val type = habit.habitType) {
                is HabitType.SingleHabit -> HabitType.Scheduled()
                else -> HabitType.SingleHabit()
            }
        )
    }

    fun deleteHabitStreak(
        habit: Habit,
        index: Int,
    ): Habit {
        return when (val type = habit.habitType) {
            is HabitType.SingleHabit -> {
                habit.copy(
                    habitType = HabitType.SingleHabit(
                        streakNames = type.streakNames.filterIndexed { i, _ ->
                            i != index
                        })
                )
            }

            else -> habit
        }

    }


    private fun changeHabitStreakAspect(
        habit: Habit,
        index: Int,
        operation: (Pair<Int, String>) -> Pair<Int, String>
    ): Habit {
        return when (val type = habit.habitType) {
            is HabitType.SingleHabit -> {
                habit.copy(
                    habitType = HabitType.SingleHabit(streakNames = type.streakNames.mapIndexed { i, pair ->
                        if (i == index) {
                            operation(pair)
                        } else {
                            pair
                        }
                    })
                )
            }

            else -> habit
        }

    }

    fun changeHabitStreakName(habit: Habit, name: String, index: Int): Habit {
        return changeHabitStreakAspect(habit, index) { pair -> pair.first to name }
    }

    fun changeHabitStreakValue(habit: Habit, streakStart: String, index: Int): Habit {
        val start = streakStart.toIntOrNull() ?: 0
        return changeHabitStreakAspect(habit, index) { pair -> start to pair.second }

    }

    fun changeHabitStartFrom(habit: Habit, startFrom: String): Habit {
        val start = startFrom.toIntOrNull() ?: 0
        return habit.copy(startFrom = start)
    }

    fun toggleHabitEnabled(habit: Habit): Habit {
        return habit.copy(enabled = !habit.enabled)
    }

    fun mapHabitAtId(habits: List<Habit>, id: Int, operation: (Habit) -> Habit): List<Habit> {
        return habits.map { habit ->
            if (id == habit.id) {
                operation(habit)
            } else {
                habit
            }
        }
    }


    fun getNewId(habits: List<Habit>): Int {
        return (habits.maxByOrNull { it.id }?.id ?: 0) + 1
    }

    fun parseHabitsToJson(habits: List<Habit>): String {
        return habits.map { it.toDTO() }.toTamaCompatString()
    }

    fun parseHabitsToJsonWithSpecials(habits: List<Habit>, specials: List<JsonElement>): String {
        return habits.map { it.toDTO() }.toTamaCompatStringWithSpecials(specials)
    }

    fun parseToJson(specials: List<JsonElement>): String {
        return parseHabitsToJsonWithSpecials(_habits.value, specials)
    }

    fun parseFromJson(json: String): List<JsonElement> {
        val (habits, specials) = parseHabitsFromJsonWithJsonAddition(json)
        _habits.value = habits
        return specials
    }

    fun parseHabitsFromJson(json: String): List<Habit> {
        // this is naive for one variation. to all first parse to json element and check for sign
        return Json.decodeFromString<Map<String, SingleHabitDTO2>>(json).values.map { it.toHabit() }
    }

    fun parseHabitsFromJsonWithJsonAddition(json: String): Pair<List<Habit>, List<JsonElement>> {
        val jsonHabits: JsonElement = Json.decodeFromString(json)
        val specialHabits: MutableList<JsonElement> = mutableListOf()
        val singleHabits: MutableList<SingleHabitDTO2> = mutableListOf()
        for (j in jsonHabits.jsonObject.values) {
            if (
                j.jsonObject["scheduled_tasks"] != null ||
                j.jsonObject["planned_task"] != null
            ) {
                println(j)
                specialHabits.add(j)
            } else {
                singleHabits.add(Json.decodeFromJsonElement(j))
            }
        }
        return singleHabits.map { it.toHabit() }.toList() to specialHabits.toList()
    }
}

fun main() {
    val string = """
       {
  "1": {
    "completed": false,
    "failed": false,
    "id": "1",
    "name": "Daily Goal",
    "planned_task": [
      { "day": 4, "month": 3, "tasks": ["hun", ""], "year": 2025 },
      { "day": 5, "month": 3, "tasks": ["innk"], "year": 2025 }
    ],
    "start_at_streak": 0
  },
  "2": {
    "completed": false,
    "failed": false,
    "id": "2",
    "name": "habits",
    "scheduled_tasks": [
      {
        "completed": false,
        "enabled": false,
        "id": "2",
        "name": "One Approach",
        "parent": "e",
        "weekdays": [1, 2, 3]
      },
      {
        "completed": false,
        "enabled": true,
        "id": "2",
        "name": "medicine",
        "parent": "e",
        "weekdays": [3]
      }
    ],
    "start_at_streak": 0
  },
  "3": {
    "completed": false,
    "failed": false,
    "id": "3",
    "name": "Sleep before 1h",
    "start_at_streak": 3,
    "streak_name": { "6": "Sleep before 12h" }
  }
} 
    """.trimIndent()
    val (habits, specials) = HabitRepository().parseHabitsFromJsonWithJsonAddition(string)
    val s2 = HabitRepository().parseHabitsToJsonWithSpecials(habits, specials)
    println(s2)

}