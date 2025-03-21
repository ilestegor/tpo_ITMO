package org.example.logarithmicFunctions

import kotlin.math.abs

class Ln : AbstractLogarithmicFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        if (x == 1.0) return 0.0

        val y = (x - 1) / (x + 1)
        var term = y
        var sum = 0.0
        var n = 1
        var compensation = 0.0

        while (abs(term) > epsilon) {
            val currentTerm = term / n
            val yToAdd = currentTerm - compensation
            val newSum = sum + yToAdd
            compensation = (newSum - sum) - yToAdd
            sum = newSum

            term *= (y * y)
            n += 2
        }

        return 2 * sum
    }
}