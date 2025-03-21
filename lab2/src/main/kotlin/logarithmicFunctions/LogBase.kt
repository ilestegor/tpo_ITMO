package org.example.logarithmicFunctions

import org.example.interfaces.Function

class LogBase(val base: Double, private val ln: Function = Ln()) : AbstractLogarithmicFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        return ln.compute(x, epsilon) / ln.compute(base, epsilon)
    }

    override fun validateInput(x: Double) {
        super.validateInput(x)
        require(base > 1 && !base.isNaN() && !base.isInfinite()) { "Log base must be real and greater than one" }
    }
}