package com.example.habitgenerator.data_layer.dto

import com.example.habitgenerator.data_layer.Habit
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("planned_habit")
data class PlannedHabitDTO(
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
    @SerialName("planned_task")
    val plannedTasks: List<PlannedTasksDTO>,
) : HabitDTO() {
    override fun toHabit(): Habit {
        TODO("Not yet implemented")
    }
}

@Serializable
data class PlannedTasksDTO(
    val date: SimpleDateDTO,
    val tasks: List<String>
)
