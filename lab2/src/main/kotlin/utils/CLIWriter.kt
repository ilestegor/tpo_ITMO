package org.example.utils

import org.example.interfaces.OutputService

class CLIWriter : OutputService<String> {
    override fun write(fileName: String, data: String) {
        println(data)
    }
}