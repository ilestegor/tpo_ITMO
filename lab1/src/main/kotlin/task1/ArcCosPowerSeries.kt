@file: JvmName("MathUtil")

package org.example.task1

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow

fun powerSeries(x: Double, tolerance: Double = 1e-10): Double {
    require(x in -1.0..1.0) { "x must be in the range [-1, 1]" }

    if (x == 1.0) return 0.0
    if (x == -1.0) return PI

    var result = PI / 2
    var term = x
    var n = 1
    val xSquared = x * x

    while (abs(term) > tolerance) {
        result -= term
        n++
        val numerator = (2 * n - 1).toDouble().pow(2)
        val denominator = (2 * n + 2) * (2 * n + 3)
        term *= xSquared * (numerator / denominator)
    }
    return result
}