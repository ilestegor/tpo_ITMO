package org.example.trigonometricFunctions

import org.example.interfaces.Function
import kotlin.math.PI
import kotlin.math.abs

class Tg(private val sin: Function = Sin(), private val cos: Function = Cos()) : AbstractTrigonometricFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        return sin.compute(x, epsilon) / cos.compute(x, epsilon)
    }

    override fun validateInput(x: Double) {
        super.validateInput(x)
        val remainder = abs((x - PI / 2) % PI) // Distance from π/2 in terms of π
        val tolerance = 1e-10 // Tolerance for floating-point comparison

        if (remainder <= tolerance || abs(remainder - PI) <= tolerance) {
            throw IllegalArgumentException("Input must not be π/2 + π*k for any integer k")
        }
    }
}