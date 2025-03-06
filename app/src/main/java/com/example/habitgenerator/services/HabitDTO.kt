package com.example.habitgenerator.services

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

interface HabitDTO {
    fun toJsonString(): String
    fun toJson(): JsonElement
    fun getId2(): Int
    fun toHabit(): Habit
}

fun Habit.toDTO(): HabitDTO {
    return when (val h = this.habitType) {
        is HabitType.SingleHabit -> {
            SimpleHabitDTO(
                id = id,
                name = name,
                completed = completed,
                failed = failed,
                enabled = enabled,
                startFrom = startFrom,
                streakName = h.streakName,
            )
        }

        else -> throw error("Unimplemented")
    }
}

@Serializable
data class SimpleHabitDTO(
    val id: Int,
    val completed: Boolean,
    val failed: Boolean,
    val enabled: Boolean = true,
    val name: String,
    val startFrom: Int = 0,
    val streakName: Map<Int, String>? = null,
) : HabitDTO {

    companion object {
        fun fromJson(json: String): SimpleHabitDTO {
            return Json.decodeFromString(json)
        }
    }

    override fun getId2(): Int = id

    override fun toHabit(): Habit = Habit(
        id = id,
        name = name,
        completed = completed,
        failed = failed,
        enabled = enabled,
        startFrom = startFrom,
        habitType = HabitType.SingleHabit(
            streakName = streakName?.toMutableMap()
        )
    )


    override fun toJsonString(): String {
        return Json.encodeToString(this)
    }

    override fun toJson(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}

fun List<HabitDTO>.toTamaCompatString(): String {
    val jsonElements = this.fold(mutableMapOf<Int, JsonElement>()) { acc, habitDTO ->
        acc.apply { put(habitDTO.getId2(), habitDTO.toJson()) }
    }.toMap()
    val mapSerializer = MapSerializer(Int.serializer(), JsonElement.serializer())
    return Json.encodeToString(mapSerializer, jsonElements)
}
