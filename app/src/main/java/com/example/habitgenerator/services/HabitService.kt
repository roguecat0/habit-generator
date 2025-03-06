package com.example.habitgenerator.services

class HabitService {
    fun changeHabitName(habit: Habit, name: String): Habit {
        return habit.copy(name = name)
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
}