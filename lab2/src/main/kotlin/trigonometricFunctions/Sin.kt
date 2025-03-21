package org.example.trigonometricFunctions

import kotlin.math.abs

class Sin : AbstractTrigonometricFunction() {
    override fun calculate(x: Double, epsilon: Double): Double {
        require(!x.isNaN() && !x.isInfinite()) { "X should be defined and can not be infinite" }
        val twoPi = 2.0 * Math.PI
        var xNorm = x - twoPi * Math.round(x / twoPi)
        if (xNorm > Math.PI) xNorm -= twoPi
        if (xNorm < -Math.PI) xNorm += twoPi


        var result = 0.0
        var term = xNorm
        var n = 1
        val xSquared = xNorm * xNorm
        var compensation = 0.0

        while (abs(term) > epsilon) {
//                Добавляем предыдущую ошибку к новому члену ряда
            val y = term - compensation
//                Добавляем к результату новый член ряда с ошибкой
            val t = result + y
//                Вычисляем то, что не влезло в t из-за округления при суммирования с ошибкой из преддыущего этапа
//                Формирование ошибки
            compensation = (t - result) - y
            result = t

            val numerator = -term * xSquared
            term = numerator / ((2.0 * n) * (2.0 * n + 1.0))
            n++
        }
        return result

    }


}