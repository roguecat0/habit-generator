package com.example.habitgenerator.data_layer.dto

import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("single_habit")
data class SingleHabitDTO(
    override val id: String,
    override val completed: Boolean,
    override val failed: Boolean,
    @Transient
    override val enabled: Boolean = true,
    override val name: String,
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    @SerialName("start_at_streak")
    override val startFrom: Int,
    @SerialName("streak_name")
    val streakNames: Map<Int, String>? = null,
) : HabitDTO() {
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
}
