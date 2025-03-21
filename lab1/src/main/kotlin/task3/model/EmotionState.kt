package org.example.task3.model
enum class Emotions {
    CONFUSION, JOY, FEAR, ANGER, SADNESS, DISGUST
}

class EmotionState(
     var confusion: Int = 0,
     var fear: Int = 0,
     var joy: Int = 0,
     var anger: Int = 0,
     var sadness: Int = 0,
     var disgust: Int = 0
) {
    fun updateEmotion(emotion: Emotions, intensity: Int) {
        when (emotion) {
            Emotions.CONFUSION -> confusion += intensity
            Emotions.JOY -> joy += intensity
            Emotions.FEAR -> fear += intensity
            Emotions.ANGER -> anger += intensity
            Emotions.SADNESS -> sadness += intensity
            Emotions.DISGUST -> disgust += intensity
        }
    }



    fun calcShockLevel(): Int {
        return confusion + fear + anger + disgust
    }
}