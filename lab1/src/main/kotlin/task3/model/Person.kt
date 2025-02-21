package org.example.task3.model

import org.example.task3.abstracts.BodyPart
import org.example.task3.bodyparts.Head
import org.example.task3.bodyparts.Jaw
import org.example.task3.bodyparts.Leg
import org.example.task3.exception.NoShockingEventOccurredException
import kotlin.random.Random
import kotlin.reflect.KClass
const val MIN_SHOCK_VALUE = 20
data class Person(val name: String) {
    private val bodyParts: MutableList<BodyPart> = mutableListOf()
    private val emotions = EmotionState()

    fun addBodyPart(vararg bParts: BodyPart){
        bodyParts.addAll(bParts)
    }

    fun shock(){
        val legsList = findBodyPart(Leg::class)
        val isOnPanel = legsList.all {x ->
            val s = x as Leg
            s.isOnSurface != null && s.isOnSurface is ControlPanel
        }
        if (isOnPanel){
            emotions.updateEmotion(Emotions.FEAR, Random.nextInt(5, 10))
            emotions.updateEmotion(Emotions.CONFUSION, Random.nextInt(50, 100))
            emotions.updateEmotion(Emotions.DISGUST, Random.nextInt(0, 5))
        } else throw NoShockingEventOccurredException("No shocking event has occurred")
    }

    fun jawDrop(){
        if (emotions.calcShockLevel() > MIN_SHOCK_VALUE){
            emotions.updateEmotion(Emotions.CONFUSION, Random.nextInt(100, 200))
            val heads = findBodyPart(Head::class)
            heads.forEach {
                x -> x as Head
                x.jaw.dropJaw()
            }
        } else throw NoShockingEventOccurredException("Jaw can not be dropped. No shocking event found")
    }

    fun findBodyPart(part: KClass<out BodyPart>): List<BodyPart>{
        return bodyParts.filterIsInstance(part.java)
    }

    fun getEmotions(): EmotionState{
        return emotions
    }
}