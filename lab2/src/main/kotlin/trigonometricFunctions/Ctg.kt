package org.example.trigonometricFunctions

import org.example.interfaces.Function
import kotlin.math.PI

class Ctg(private val sin: Function = Sin(), private val cos: Function = Cos()) : AbstractTrigonometricFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        return cos.compute(x, epsilon) / sin.compute(x, epsilon)
    }

    override fun validateInput(x: Double) {
        super.validateInput(x)
        require(x % PI != 0.0) { "Cot is not defined in this point" }
    }
}