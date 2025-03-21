package org.example.interfaces

interface OutputService<T> {
    fun write(fileName: String, data: T)
}