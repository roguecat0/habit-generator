package com.example.habitgenerator.services.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ScheduledHabitDTO2 @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: String,
    val completed: Boolean,
    val failed: Boolean,
    @Transient
    val enabled: Boolean = true,
    val name: String,
    @EncodeDefault
    @SerialName("start_at_streak")
    val startFrom: Int = 0,
//    @Serializable("scheduled_tasks")
)
