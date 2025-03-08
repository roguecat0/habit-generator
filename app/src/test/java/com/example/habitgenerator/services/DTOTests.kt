package com.example.habitgenerator.services

import com.example.habitgenerator.services.dto.HabitDTO
import com.example.habitgenerator.services.dto.SingleHabitDTO
import com.example.habitgenerator.services.dto.toDTO
import com.example.habitgenerator.services.dto.toTamaCompatString
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
        val dto: HabitDTO = SingleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        val str = dto.toJsonString()
        println(str)
        assert(str.isNotEmpty())
    }

    @Test
    fun makesString2() {
        val string =
            """{"id":"3","completed":true,"failed":false,"name":"lol","start_at_streak":3}"""
        val dto: HabitDTO = SingleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        val str = dto.toJsonString()
        println(str)
        assertEquals(str, string)
    }

    @Test
    fun makesString3() {
        val string =
            """ { "completed": false, "enabled": false, "failed": false, "id": "1", "name": "Morning Routine", "start_at_streak": 0 }
            """.trimIndent()
        val dto: HabitDTO = SingleHabitDTO(
            "1", true, false, true, "Morning Routine", 1
        )
        val str = dto.toJsonString()
        println(str)
    }

    @Test
    fun fromString() {
        val dto: HabitDTO = SingleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        val str = dto.toJsonString()
        println(str)
        assert(str.isNotEmpty())
    }

    @Test
    fun fromStringMap() {
        val string = """
    {
  "1": {
    "completed": false,
    "failed": false,
    "id": "1",
    "name": "Morning Routine",
    "start_at_streak": 0
  }
}
    """.trimMargin()
        println(string)
        val dto = Json.decodeFromString<Map<String, SingleHabitDTO>>(string)
        println(dto)

        val mapp = mapOf("1" to SingleHabitDTO("1", false, false, true, "Morning Routine", 0))
        assertEquals(dto, mapp)
    }


    @Test
    fun makeObject() {
        val string =
            "{\"id\":\"3\",\"completed\":true,\"failed\":false,\"name\":\"lol\",\"start_at_streak\":3}"
        val dto: HabitDTO = SingleHabitDTO(
            "3", true, false, true, "lol", 3
        )
        assertEquals(SingleHabitDTO.Companion.fromJson(string), dto)
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
        val dto: HabitDTO = SingleHabitDTO(
            "3", true, false, true, "lol",
        )
        assertEquals(SingleHabitDTO.fromJson(string), dto)
    }

    @Test
    fun MultiPart() {
        val list: List<HabitDTO> = listOf(
            SingleHabitDTO(
                "3", true, false, true, "lol",
            ),
            SingleHabitDTO(
                "2", true, false, true, "later", startFrom = 4
            ),
            SingleHabitDTO(
                "8", true, false, true, "lol", streakNames = mapOf(
                    3 to "cool",
                    2 to "spectacular"
                )
            ),
        )
        val tamaJson = list.toTamaCompatString()
        println(tamaJson)
        assert(tamaJson.isNotEmpty())
    }

    @Test
    fun toHabit() {
        val dto = SingleHabitDTO(
            "0", completed = true, failed = true,
            enabled = false, name = "yolo", startFrom = 3, streakNames = mapOf(3 to "lol")
        )
        val habit = Habit(
            0, "yolo", false, 3,
            completed = true, failed = true, habitType = HabitType.SingleHabit(
                listOf(3 to "lol")
            )
        )
        val habit2 = dto.toHabit()
        assertEquals(habit, habit2)
    }

    @Test
    fun toSimpleDTO() {
        val dto = SingleHabitDTO(
            "0",
            completed = true,
            failed = true,
            enabled = false,
            name = "yolo",
            startFrom = 103,
            streakNames = mapOf(3 to "lol") // because hack
        )
        val habit = Habit(
            0, "yolo", false, 3,
            completed = true, failed = true, habitType = HabitType.SingleHabit(
                listOf(3 to "lol")
            )
        )
        val dto2 = habit.toDTO()
        assertEquals(dto, dto2)
    }
}