package org.example.utils

import org.example.interfaces.DataGenerator
import org.example.interfaces.Function

class FunctionDataGenerator : DataGenerator<List<Pair<Double, Double>>> {
    override fun generateData(start: Double, end: Double, step: Double, func: Function): List<Pair<Double, Double>> {
        val data = mutableListOf<Pair<Double, Double>>()
        val steps = ((end - start) / step).toInt()
        for (n in 0..steps) {
            val current = start + n * step
            val yValue = try {
                func.compute(current)
            } catch (e: IllegalArgumentException) {
                Double.NaN
            }
            data.add(current to yValue)
        }
        return data
    }
}