package org.example.task3.bodyparts

import org.example.task3.abstracts.BodyPart
import org.example.task3.exception.BodyPartIsBusyException
import org.example.task3.exception.JawAlreadyDroppedException
import org.example.task3.exception.JawClosedException

enum class SmileType {
    SMILE, GRIN, SAD, NONE
}

class Jaw(teeth: Teeth = Teeth()) : BodyPart("Jaw") {
    var isDropped = false
    var smileType: SmileType = SmileType.NONE
    lateinit var head: Head
    val teeth = teeth.apply { jaw = this@Jaw } // Устанавливаем связь зубов с челюстью

    fun dropJaw(){
        if (!isDropped)
            isDropped = true
        else throw JawAlreadyDroppedException("Jaw is already dropped")
    }

    fun closeJaw(){
        if (isDropped)
            isDropped = false
        else throw JawClosedException("Jaw is already closed")
    }

    fun smile(smile: SmileType, head: BodyPart){
        if (head is Head){
            smileType = when (smile){
                SmileType.SMILE -> SmileType.SMILE
                SmileType.GRIN -> SmileType.GRIN
                SmileType.SAD -> SmileType.SAD
                SmileType.NONE -> SmileType.NONE
            }
            if (!isBusy){
                head.isBusy = true
                isBusy = true
            } else throw BodyPartIsBusyException("Head is busy")
        }

    }
}