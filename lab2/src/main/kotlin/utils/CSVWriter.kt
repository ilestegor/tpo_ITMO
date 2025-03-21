package org.example.utils

import org.example.interfaces.OutputService
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CSVWriter(private val defaultFolder: String = "/src/main/resources") : OutputService<List<Pair<Double, Double>>> {
    override fun write(fileName: String, data: List<Pair<Double, Double>>) {
        val currentWorkingDirectory = System.getProperty("user.dir")
        val resultFilePath = currentWorkingDirectory + File.separator + defaultFolder
        val resultDir = File(resultFilePath)
        if (!resultDir.exists()) {
            resultDir.mkdirs()
        }


        val filePath =
            "$resultFilePath${File.separator}${fileName}_${
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            }.csv"

        PrintWriter(filePath).use { writer ->
            writer.println("X,Y")
            data.forEach { (x, y) ->
                writer.println("$x, $y")
            }
        }
        println("CSV saved at: $filePath")
    }
}