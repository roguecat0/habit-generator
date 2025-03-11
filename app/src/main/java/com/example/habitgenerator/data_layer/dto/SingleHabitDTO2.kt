package com.example.habitgenerator.data_layer.dto

import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class SingleHabitDTO2 @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: String,
    val completed: Boolean,
    val failed: Boolean,
    @Transient
    val enabled: Boolean = true,
    val name: String,
    @EncodeDefault
    @SerialName("start_at_streak")
    val startFrom: Int = 0,
    @SerialName("streak_name")
    val streakNames: Map<Int, String>? = null,
) : HabitDTO2 {

    companion object {
        fun fromJson(json: String): SingleHabitDTO2 {
            return Json.decodeFromString(json)
        }
    }

    override fun getId2(): String = id

    override fun toHabit(): Habit = Habit(
        id = id.toInt(),
        name = name,
        completed = completed,
        failed = failed,
        // dirty quick fix
        // todo: make normal better enabled functionality in other app
        enabled = if (startFrom >= 100) false else enabled,
        startFrom = if (startFrom >= 100) startFrom - 100 else startFrom,
        habitType = HabitType.SingleHabit(
            streakNames = streakNames?.toList() ?: listOf()
        )
    )

    override fun toJsonString(): String {
        return Json.encodeToString(this)
    }

    override fun toJson(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}

