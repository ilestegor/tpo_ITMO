package org.example.task3.bodyparts

import org.example.task3.abstracts.BodyPart
import org.example.task3.exception.BodyPartIsBusyException

class Head(name: String) : BodyPart(name) {
    val jaw = Jaw().apply {
        head = this@Head
    }


    fun smile(smileType: SmileType){
        if (!this.isBusy)
            jaw.smile(smileType, this)
        else throw BodyPartIsBusyException("Head is busy at the moment")
    }
}
