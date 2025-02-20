package org.example.task3.interfaces

import org.example.task3.abstracts.BodyPart

interface IBodyFunction {
    fun execute(owner: BodyPart, target: BodyPart? = null)
}