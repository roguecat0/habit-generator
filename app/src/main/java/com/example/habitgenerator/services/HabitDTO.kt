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
    fun getId2(): String
}

@Serializable
data class SimpleHabitDTO(
    val id: String,
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
    override fun getId2(): String {
        return id
    }

    override fun toJsonString(): String {
        return Json.encodeToString(this)
    }

    override fun toJson(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}
fun List<HabitDTO>.toTamaCompatString(): String {
    val jsonElements = this.fold(mutableMapOf<String, JsonElement>()) { acc, habitDTO ->
        acc.apply { put(habitDTO.getId2(), habitDTO.toJson())}
    }.toMap()
    val mapSerializer = MapSerializer(String.serializer(), JsonElement.serializer())
    return Json.encodeToString(mapSerializer,jsonElements)
}
