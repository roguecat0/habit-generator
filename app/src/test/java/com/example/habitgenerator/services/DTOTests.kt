package com.example.habitgenerator.services

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.*
import org.junit.Test

class HabitDTOTest {
    @Test
    fun init() {
        assertEquals(0, 0)
    }

    @Test
    fun makesString() {
        val dto: HabitDTO = SimpleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        val str = dto.toJsonString()
        println(str)
        assert(str.isNotEmpty())
    }

    @Test
    fun fromString() {
        val dto: HabitDTO = SimpleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        val str = dto.toJsonString()
        println(str)
        assert(str.isNotEmpty())
    }

    @Test
    fun makeObject() {
        val string =
            "{\"id\":\"3\",\"completed\":true,\"failed\":false,\"name\":\"lol\",\"startFrom\":3}"
        val dto: HabitDTO = SimpleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        assertEquals(SimpleHabitDTO.Companion.fromJson(string), dto)
    }

    @Test
    fun partialdataObject() {
        val string =
            """
                {
                "id":"3",
                "completed":true,
                "failed":false,
                "name":"lol"
                }
            """.trimMargin()
        val dto: HabitDTO = SimpleHabitDTO(
            "3", true, false, true, "lol",
        )
        assertEquals(SimpleHabitDTO.fromJson(string), dto)
    }

    @Test
    fun MultiPart() {
        val list: List<HabitDTO> = listOf(
            SimpleHabitDTO(
                "3", true, false, true, "lol",
            ),
            SimpleHabitDTO(
                "4", true, false, true, "later", startFrom = 4
            ),
            SimpleHabitDTO(
                "5", true, false, true, "lol", streakName = mapOf(
                    3 to "cool",
                    2 to "spectacular"
                )
            ),
        )
        val tamaJson = list.toTamaCompatString()
        println(tamaJson)
        assert(tamaJson.isNotEmpty())
    }
}