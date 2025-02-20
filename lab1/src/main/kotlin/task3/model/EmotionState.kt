package org.example.task3.model
enum class Emotions {
    CONFUSION, JOY, FEAR, ANGER, SADNESS, DISGUST
}

class EmotionState(
    private var confusion: Int = 0,
    private var fear: Int = 0,
    private var joy: Int = 0,
    private var anger: Int = 0,
    private var sadness: Int = 0,
    private var disgust: Int = 0
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

    fun getEmotionState(): String {
        return "Confusion: $confusion, Fear: $fear, Joy: $joy, Anger: $anger, Sadness: $sadness, Disgust: $disgust"
    }


    fun calcShockLevel(): Int {
        return confusion + fear + anger + disgust
    }
}