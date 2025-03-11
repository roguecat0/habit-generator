package com.example.habitgenerator.data_layer.util

fun <T, E> List<Pair<T, E>>.splitPairs(): Pair<List<T>, List<E>> {
    return this.fold(listOf<T>() to listOf<E>()) { acc, pair ->
        val habits = acc.first + pair.first
        val expandedItems = acc.second + pair.second
        habits to expandedItems
    }
}