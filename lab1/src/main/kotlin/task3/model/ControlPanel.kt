package org.example.task3.model

import org.example.task3.interfaces.Device
import org.example.task3.interfaces.Surface

class ControlPanel(private var isOn: Boolean = false, var isAvailableSurface: Boolean = true) : Device, Surface {

    override fun turnOn(){
        if (!isOn)
            isOn = true
    }

    override fun turnOff() {
        if (isOn)
            isOn = false
    }

    override fun isAvailable(): Boolean {
        return isAvailableSurface
    }

}