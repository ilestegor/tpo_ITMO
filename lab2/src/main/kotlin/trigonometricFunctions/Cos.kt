package org.example.trigonometricFunctions

import org.example.interfaces.Function
import kotlin.math.PI

class Cos(private val sin: Function = Sin()) : AbstractTrigonometricFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        return sin.compute(PI / 2 - x, epsilon)
    }
}