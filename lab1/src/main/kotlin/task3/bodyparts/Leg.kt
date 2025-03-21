package org.example.task3.bodyparts

import org.example.task3.abstracts.BodyPart
import org.example.task3.exception.SurfaceIsBusyException
import org.example.task3.interfaces.Placeable
import org.example.task3.interfaces.Surface

class Leg(side: Sides) : BodyPart("Leg"), Placeable {
    var isOnSurface: Surface? = null

    override fun placeOn(surface: Surface) {
        if (surface.isAvailable()) {
            isOnSurface = surface
        } else throw SurfaceIsBusyException("Surface is busy")
    }

    override fun removeFromSurface() {
        isOnSurface = null
    }
}