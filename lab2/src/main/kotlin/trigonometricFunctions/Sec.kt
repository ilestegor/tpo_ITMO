package org.example.trigonometricFunctions

import org.example.interfaces.Function
import kotlin.math.PI
import kotlin.math.abs

class Sec(private val cos: Function = Cos()) : AbstractTrigonometricFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        return 1 / cos.compute(x, epsilon)
    }

    override fun validateInput(x: Double) {
        super.validateInput(x)
        val remainder = abs((x - PI / 2) % PI)
        val tolerance = 1e-10

        require(remainder >= tolerance && abs(remainder - PI) >= tolerance) { "Sec is not defined at this point" }
    }
}