package org.example.system

import org.example.interfaces.Function
import org.example.logarithmicFunctions.Ln
import org.example.logarithmicFunctions.LogBase
import org.example.trigonometricFunctions.*

abstract class AbstractFunctionSystem : Function {
    protected val sin: Function = Sin()
    protected val cos: Function = Cos()
    protected val tan: Function = Tg()
    protected val cot: Function = Ctg()
    protected val sec: Function = Sec()
    protected val csc: Function = Csc()

    protected val ln: Function = Ln()
    protected val logBase2: Function = LogBase(2.0)
    protected val logBase3: Function = LogBase(3.0)
    protected val logBase5: Function = LogBase(5.0)

    override fun compute(x: Double, epsilon: Double): Double {
        return functionSystem(x)
    }

    protected abstract fun functionSystem(x: Double): Double
}