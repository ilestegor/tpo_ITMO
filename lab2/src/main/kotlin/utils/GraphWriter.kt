package org.example.utils

import org.example.interfaces.OutputService
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChartBuilder
import java.io.File
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GraphWriter(private val outputFolder: String = "src/main/resources/images") :
    OutputService<List<Pair<Double, Double>>> {

    override fun write(fileName: String, data: List<Pair<Double, Double>>) {
        val xData = data.map { it.first }
        val yData = data.map { it.second }

        val chart = XYChartBuilder()
            .width(800)
            .height(600)
            .title("Generated Graph")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build()

        chart.addSeries("f(x)", xData, yData)

        val outputDir = Paths.get(System.getProperty("user.dir"), outputFolder).toFile()
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val filePath = "$outputFolder${File.separator}${fileName}_$formattedTime.png"

        BitmapEncoder.saveBitmap(chart, filePath, BitmapEncoder.BitmapFormat.PNG)
        println("Graph saved to: $filePath")
    }

    fun readCSVAndSaveGraph(filePath: String, outputFileName: String) {
        val xData = mutableListOf<Double>()
        val yData = mutableListOf<Double>()

        File(filePath).useLines { lines ->
            lines.drop(1).forEach { line ->
                val parts = line.split(",")
                val x = parts.getOrNull(0)?.toDoubleOrNull()
                val y = parts.getOrNull(1)?.toDoubleOrNull()
                if (x != null && y != null && y in -5.0..5.0) {
                    xData.add(x)
                    yData.add(y)
                }
            }
        }

        write(outputFileName, xData.zip(yData))
    }
}