package com.example.habitgenerator.data_layer

import org.junit.Assert.*

import org.junit.Test

class SimpleSimpleDateDTOTest {

    @Test
    fun getDay() {
        var simpleDate = SimpleDate(0, 0, 0)
        println("did somethign")
        assertEquals(simpleDate.day, 1)
    }

    @Test
    fun somethign() {
        val sd = SimpleDate(1, 1, 1)
        assertNotEquals(sd.month, 0)
    }
}

class TestTwo {
    @Test
    fun anotherTest() {
        assertEquals(0, 1 + 2)
    }
}