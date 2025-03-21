package UnitTest

import org.example.trigonometricFunctions.Csc
import org.example.trigonometricFunctions.Sin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI


class SinUnitTest {
    private val EPSILON = 1e-7

    companion object {

        private val sinMockData: MutableMap<Double, Double> = mutableMapOf()


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
            val sinFiles = listOf("/sin_mock.csv")
            loadDataIntoMap(sinFiles, sinMockData)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, PI, PI / 2, 1.5 * PI, 2 * PI])
    fun cornerCasesForSin(x: Double) {
        val sinMock = Mockito.mock(Csc::class.java)

        whenever(sinMock.compute(x)).thenReturn(sinMockData[x])
        assertEquals(sinMock.compute(x), Sin().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [PI / 6, (3 * PI) / 4, 1.2 * PI, 1.7 * PI])
    fun equivalentCasesForSin(x: Double) {
        val sinMock = Mockito.mock(Csc::class.java)

        whenever(sinMock.compute(x)).thenReturn(sinMockData[x])
        assertEquals(sinMock.compute(x), Sin().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [-PI / 6, -0.75 * PI, -1.2 * PI, -1.7 * PI])
    fun negativeCasesForSin(x: Double) {
        val sinMock = Mockito.mock(Csc::class.java)

        whenever(sinMock.compute(x)).thenReturn(sinMockData[x])
        assertEquals(sinMock.compute(x), Sin().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun invalidInputTest(x: Double) {
        assertThrows<IllegalArgumentException> { Sin().compute(x) }
    }

}