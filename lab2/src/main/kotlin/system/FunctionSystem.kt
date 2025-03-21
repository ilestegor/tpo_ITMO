package org.example.system

import org.example.interfaces.Function
import org.example.logarithmicFunctions.Ln
import org.example.logarithmicFunctions.LogBase
import org.example.trigonometricFunctions.*
import kotlin.math.pow

class FunctionSystem(
    private val sin: Function = Sin(),
    private val cos: Function = Cos(),
    private val tan: Function = Tg(),
    private val cot: Function = Ctg(),
    private val sec: Function = Sec(),
    private val csc: Function = Csc(),

    private val ln: Function = Ln(),
    private val logBase2: Function = LogBase(2.0),
    private val logBase3: Function = LogBase(3.0),
    private val logBase5: Function = LogBase(5.0)
) : Function {


    override fun compute(x: Double, epsilon: Double): Double {
        require(x != 1.0) { "Logarithmic part is not defined in this Point" }
        return if (x <= 0)
            ((((((sec.compute(x, epsilon) / cos.compute(x, epsilon)).pow(3)) - (sin.compute(x, epsilon) - cot.compute(
                x,
                epsilon
            ))) + csc.compute(
                x, epsilon
            )) * (cot.compute(x, epsilon) / sec.compute(x, epsilon))) / ((tan.compute(x, epsilon) + cos.compute(
                x,
                epsilon
            )) + (tan.compute(x, epsilon)
                .pow(3))))
        else
            (((((logBase5.compute(x, epsilon) + logBase3.compute(x, epsilon)).pow(2)) * logBase3.compute(
                x,
                epsilon
            )) + (logBase2.compute(x, epsilon) * (logBase2.compute(
                x
            ) + ln.compute(x, epsilon)))) * ((logBase3.compute(x, epsilon).pow(3)) / (logBase3.compute(
                x,
                epsilon
            ) - logBase2.compute(x, epsilon))))
    }
}