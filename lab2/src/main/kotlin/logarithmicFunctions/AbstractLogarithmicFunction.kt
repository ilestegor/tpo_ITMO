package org.example.logarithmicFunctions

import org.example.interfaces.Function

abstract class AbstractLogarithmicFunction : Function {

    abstract fun calculate(x: Double, epsilon: Double = 1e-15): Double

    override fun compute(x: Double, epsilon: Double): Double {
        validateInput(x)
        return calculate(x, epsilon)
    }

    protected open fun validateInput(x: Double) {
        require(!x.isNaN() && !x.isInfinite() && x > 0.0) { "X must be real number greater than zero" }
    }
}