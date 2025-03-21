package UnitTest

import org.example.logarithmicFunctions.Ln
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths

class LnUnitTest {
    private val EPSILON: Double = 1e-7

    companion object {

        private val lnMockData: MutableMap<Double, Double> = mutableMapOf()


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
            val lnFiles = listOf("/ln_mock_values.csv")
            loadDataIntoMap(lnFiles, lnMockData)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun shouldThrowException(x: Double) {
        assertThrows<IllegalArgumentException> { Ln().compute(x) }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.5, 1.0, 4.0])
    fun shouldCalculateLn(x: Double) {
        val lnMock = Mockito.mock(Ln::class.java)

        whenever(lnMock.compute(x)).thenReturn(lnMockData[x])
        assertEquals(lnMock.compute(x), Ln().compute(x), EPSILON)
    }
}