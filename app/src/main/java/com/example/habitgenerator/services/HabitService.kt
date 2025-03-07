package com.example.habitgenerator.services

import android.util.Log
import kotlinx.serialization.json.Json

const val TAG = "HabitService"

class HabitService {
    fun changeHabitName(habit: Habit, name: String): Habit {
        return habit.copy(name = name)
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
        return habits.map { it.toDTO() }.let { Log.d(TAG, "parseHabitsToJson: $it");it }
            .toTamaCompatString()
    }

    fun parseHabitsFromJson(json: String): List<Habit> {
        // this is naive for one variation. to all first parse to json element and check for sign
        return Json.decodeFromString<Map<String, SimpleHabitDTO>>(json).values.map { it.toHabit() }
    }
}