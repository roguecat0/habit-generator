package com.example.habitgenerator.services

import kotlinx.serialization.json.Json
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
                "3", true, false, true, "lol",
            ),
        )
        val jsonElements = list.map { it.toJson() }
        println(Json.encodeToString())
    }
}