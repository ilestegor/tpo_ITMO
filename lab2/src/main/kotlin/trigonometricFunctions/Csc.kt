package org.example.trigonometricFunctions

import org.example.interfaces.Function
import kotlin.math.PI

class Csc(private val sin: Function = Sin()) : AbstractTrigonometricFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        return 1 / sin.compute(x, epsilon)
    }

    override fun validateInput(x: Double) {
        super.validateInput(x)
        require(x % PI != 0.0) { "Csc is not defined in this point" }
    }
}