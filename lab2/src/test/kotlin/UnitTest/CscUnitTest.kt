package UnitTest


import org.example.trigonometricFunctions.Csc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI

class CscUnitTest {
    private val EPSILON = 1e-7

    companion object {

        private val cscMockData: MutableMap<Double, Double> = mutableMapOf()


        private fun loadDataIntoMap(files: List<String>, targetMap: MutableMap<Double, Double>) {
            files.forEach { file ->
                Files.readAllLines(Paths.get("src/test/resources$file"))
                    .map { line ->
                        val (x, y) = line.split(",").map { it.trim().toDouble() }
                        x to y
                    }
                    .forEach { (x, y) ->
                        targetMap[x] = y
                    }
            }
        }

        init {
            val cscFiles = listOf("/csc_mock_values.csv")
            loadDataIntoMap(cscFiles, cscMockData)
        }
    }


    @ParameterizedTest
    @ValueSource(doubles = [PI / 6, 0.7 * PI, -PI / 6, -0.7 * PI])
    fun shouldCalculateCsc(x: Double) {
        val cscMock = Mockito.mock(Csc::class.java)

        whenever(cscMock.compute(x)).thenReturn(cscMockData[x])
        assertEquals(cscMock.compute(x), Csc().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [-PI, 0.0, PI, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun invalidInputTest(x: Double) {
        assertThrows<IllegalArgumentException> { Csc().compute(x) }
    }
}