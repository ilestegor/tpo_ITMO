package org.example.task3.model

import org.example.task3.interfaces.Surface

class ControlPanel(private var isOn: Boolean = false, var isAvailableSurface: Boolean = true) : Surface {

    override fun isAvailable(): Boolean {
        return isAvailableSurface
    }

}