package com.example.habitgenerator.data_layer

import android.util.Log
import com.example.habitgenerator.data_layer.dto.HabitDTO
import com.example.habitgenerator.data_layer.dto.PlannedHabitDTO
import com.example.habitgenerator.data_layer.dto.SingleHabitDTO2
import com.example.habitgenerator.data_layer.dto.toDTO
import com.example.habitgenerator.data_layer.dto.toHabitDTO
import com.example.habitgenerator.data_layer.dto.toTamaCompatString
import com.example.habitgenerator.data_layer.dto.toTamaCompatStringWithSpecials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlin.random.Random

const val TAG = "HabitRepository"

class HabitRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private var specials: List<HabitDTO> = emptyList()
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> get() = _habits
    fun changeHabitName(habit: Habit, name: String): Habit {
        return habit.copy(name = name)
    }

    fun deleteHabit(id: Int) {
        _habits.value = _habits.value.filter { habit -> habit.id != id }
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
                    id = Random.nextInt(),
                    scheduledType = ScheduledType.Weekdays()
                )
            )
        }
    }

    fun addScheduledInterval(habit: Habit): Habit {
        return changeScheduleValue(habit) { scheduled ->
            scheduled.copy(
                scheduledHabits = scheduled.scheduledHabits + ScheduledHabit(
                    id = Random.nextInt(),
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

    private fun mapHabitAtId(
        habits: List<Habit>,
        id: Int,
        operation: (Habit) -> Habit
    ): List<Habit> {
        return habits.map { habit ->
            if (id == habit.id) {
                operation(habit)
            } else {
                habit
            }
        }
    }


    private fun getNewId(habits: List<Habit>): Int {
        return (habits.maxByOrNull { it.id }?.id ?: 0) + 1
    }

    private fun parseHabitsToJsonWithSpecials(
        habits: List<Habit>,
        specials: List<HabitDTO>
    ): String {
        val dtoMap = (habits.map { it.toHabitDTO() } + specials)
            .fold(mapOf<String, HabitDTO>()) { acc, dto ->
                acc + (dto.id to dto)
            }
        return this.json.encodeToString(dtoMap)
    }

    private fun polymorphicParseFromJson(json: String): Map<String, HabitDTO> {
        return this.json.decodeFromString<Map<String, HabitDTO>>(json)
    }

    fun parseToJson(): String {
        return parseHabitsToJsonWithSpecials(_habits.value, this.specials)
    }

    fun parseFromJson(json: String) {
        val dtos: List<HabitDTO> = polymorphicParseFromJson(json).values.toList()
        _habits.value = dtos.filter { it !is PlannedHabitDTO }.map { it.toHabit() }
        this.specials = dtos.filterIsInstance<PlannedHabitDTO>()
    }
}