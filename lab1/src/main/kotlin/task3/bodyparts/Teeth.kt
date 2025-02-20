package org.example.task3.bodyparts

import org.example.task3.abstracts.BodyPart

class Teeth(amount: Int = 32) : BodyPart("Teeth") {
    var isBeingPicked = false
    lateinit var jaw: Jaw
}

