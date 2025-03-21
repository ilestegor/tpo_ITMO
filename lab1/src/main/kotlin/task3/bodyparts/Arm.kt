package org.example.task3.bodyparts

import org.example.task3.abstracts.BodyPart
import org.example.task3.exception.BodyPartIsBusyException
import org.example.task3.interfaces.Manipulate

enum class Sides{
    LEFT, RIGHT
}

class Arm(armSide: Sides) : BodyPart("Arm"), Manipulate {

    override fun pick(target: BodyPart) {
        target as Teeth
        val jaw = target.jaw
        val head = jaw.head

        // Проверяем занятость всех частей тела
        if (isReadyForPicking(jaw, head, target)) {
            isBusy = true
            target.isBeingPicked = true
            target.isBusy = true
            jaw.isBusy = true
            head.isBusy = true
        } else {
            throw BodyPartIsBusyException("Невозможно ковырять: часть тела занята")
        }
    }

    private fun isReadyForPicking(jaw: Jaw, head: Head, target: Teeth): Boolean {
        return !isBusy && !target.isBeingPicked && !jaw.isBusy && !head.isBusy
    }
}