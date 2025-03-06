package com.example.habitgenerator.services

import android.util.Log

const val TAG = "HabitService"

class HabitService {
    fun changeHabitName(habit: Habit, name: String): Habit {
        return habit.copy(name = name)
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
        val start = streakStart.toInt()
        return changeHabitStreakAspect(habit, index) { pair -> start to pair.second }

    }

    fun changeHabitStartFrom(habit: Habit, startFrom: String): Habit {
        val start = startFrom.toInt()
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
}