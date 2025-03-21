package org.example.interfaces

interface DataGenerator<T> {
    fun generateData(start: Double, end: Double, step: Double, func: Function): T
}