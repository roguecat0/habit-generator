package com.example.habitgenerator.services

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SerializationTest {

}

@Serializable
sealed class Object {
    abstract val value1: String
}

@Serializable
@SerialName("objectA")
data class ObjectA(
    override val value1: String,
    val subValueA: List<SubObject>
) : Object()

@Serializable
sealed class SubObject

@Serializable
@SerialName("subObjectA")
data class SubObjectA(
    val a: String
) : SubObject()

@Serializable
@SerialName("subObjectB")
data class SubObjectB(
    val b: Int
) : SubObject()


fun main() {
    val obj: Object = ObjectA(
        "hello",
        subValueA = listOf(
            SubObjectA("cool"),
            SubObjectB(1),
        )
    )
    val str = Json.encodeToString(obj)
    val objIn = Json.decodeFromString<Object>(str)
    println(str)
    println(objIn)
}