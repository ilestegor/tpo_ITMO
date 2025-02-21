package org.example

import org.example.task2.dijkstra
import org.example.task2.getShortestPath
import org.example.task2.readGraph
import org.example.task2.readGraphFromFile
import org.example.task3.bodyparts.*
import org.example.task3.model.Person


interface Clickable {
    fun click() = println("Button clciked")
}
interface Focusable {
    fun click() = println("Focused")
}
internal open class Button : Clickable, Focusable {
    init {

    }
    private var s = 4

    val getS: Int
        get() = s

    fun setS(value: Int){
        s = value
    }
    override fun click(){
        super<Clickable>.click()
        super<Focusable>.click()
    }
}

class Outer private constructor(){
      class Inner {
        fun getOuterRef() = println("Hello")
    }
}
fun main() {
    println("Hello")

}
