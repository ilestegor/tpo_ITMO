package org.example.interfaces

interface Function {
    fun compute(x: Double, epsilon: Double = 1e-15): Double
}