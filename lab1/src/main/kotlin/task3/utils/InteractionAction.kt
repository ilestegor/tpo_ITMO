package org.example.task3.utils

import org.example.task3.abstracts.BodyPart
import org.example.task3.interfaces.IBodyFunction

class InteractionAction(val description: String, private val effect: (BodyPart, BodyPart?) -> Unit) : IBodyFunction{
    override fun execute(owner: BodyPart, target: BodyPart?) {
        effect(owner, target)
    }
}