package com.example.habitgenerator.data_layer.dto

import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitType
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

interface HabitDTO2 {
    fun toJsonString(): String
    fun toJson(): JsonElement
    fun getId2(): String
    fun toHabit(): Habit
}

fun Habit.toDTO(): HabitDTO2 {
    return when (val h = this.habitType) {
        is HabitType.SingleHabit -> {
            SingleHabitDTO2(
                id = id.toString(),
                name = name,
                completed = completed,
                failed = failed,
                enabled = enabled,
                startFrom = if (!enabled) startFrom + 100 else startFrom,
                streakNames = h.streakNames.takeIf { it.isNotEmpty() }?.toMap(),
            )
        }

        else -> throw error("Unimplemented")
    }
}

fun List<HabitDTO2>.toTamaCompatString(): String {
    val jsonElements = this.fold(mutableMapOf<String, JsonElement>()) { acc, habitDTO ->
        acc.apply { put(habitDTO.getId2(), habitDTO.toJson()) }
    }.toMap()
    val mapSerializer = MapSerializer(String.serializer(), JsonElement.serializer())
    return Json.encodeToString(mapSerializer, jsonElements)
}

fun List<HabitDTO2>.toTamaCompatStringWithSpecials(specials: List<JsonElement>): String {
    val tmp = this.fold(mutableMapOf<String, JsonElement>()) { acc, habitDTO ->
        acc.apply { put(habitDTO.getId2(), habitDTO.toJson()) }
    }.toMap()
    val jsonElements = specials.fold(tmp) { acc, jsonE ->
        val p = jsonE.jsonObject["id"]?.jsonPrimitive?.contentOrNull ?: ""
        acc + (p to jsonE)
    }
    val mapSerializer = MapSerializer(String.serializer(), JsonElement.serializer())
    return Json.encodeToString(mapSerializer, jsonElements)
}
