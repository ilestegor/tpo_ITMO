package org.example.task3.abstracts

import org.example.task3.exception.FunctionDoesNotExist
import org.example.task3.interfaces.IBodyFunction

abstract class BodyPart(private val bodyPartName: String) {
    private val bodyPartSeparateFunctions = mutableMapOf<String, IBodyFunction>()
    open var isBusy = false

    fun addFunction(name: String, function: IBodyFunction) {
        bodyPartSeparateFunctions[name] = function
    }

    fun removeFunction(name: String) {
        bodyPartSeparateFunctions.remove(name)
    }

    fun executeFunctionByName(name: String, owner: BodyPart, target: BodyPart? = null) {
        bodyPartSeparateFunctions[name]?.execute(owner, target)
            ?: throw FunctionDoesNotExist("Body part does not have specified function ")
    }
    fun getBodyPartSeparateFunctionsSize(): Int{
        return bodyPartSeparateFunctions.size
    }
}