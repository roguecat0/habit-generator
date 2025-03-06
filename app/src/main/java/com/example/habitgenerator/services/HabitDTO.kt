package com.example.habitgenerator.services

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

interface HabitDTO {
    fun toJsonString(): String
    fun toJson(): JsonElement
}

@Serializable
data class SimpleHabitDTO(
    val id: String,
    val completed: Boolean,
    val failed: Boolean,
    val enabled: Boolean = true,
    val name: String,
    val startFrom: Int = 0,
) : HabitDTO {

    companion object {
        fun fromJson(json: String): SimpleHabitDTO {
            return Json.decodeFromString(json)
        }
    }

    override fun toJsonString(): String {
        return Json.encodeToString(this)
    }

    override fun toJson(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}
