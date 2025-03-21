package UnitTest

import org.example.trigonometricFunctions.Csc
import org.example.trigonometricFunctions.Tg
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI
import kotlin.math.tan


class TanUnitTest {
    private val EPSILON: Double = 1e-7

    companion object {

        private val tanMockData: MutableMap<Double, Double> = mutableMapOf()


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
            val tanFiles = listOf("/tan_mock_values.csv")
            loadDataIntoMap(tanFiles, tanMockData)
        }
    }

    @Test
    fun cornerCaseForTan() {
        assertEquals(tan(0.0), Tg().compute(0.0))
    }

    @ParameterizedTest
    @ValueSource(doubles = [-PI / 6, PI / 4, PI / 2 - 1e-3, PI / 2 + 1e-3])
    fun equivalentRegion(x: Double) {
        val tanMock = Mockito.mock(Csc::class.java)

        whenever(tanMock.compute(x)).thenReturn(tanMockData[x])
        assertEquals(tanMock.compute(x), Tg().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [PI / 2, -PI / 2, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun invalidInputTest(x: Double) {
        assertThrows<IllegalArgumentException> { Tg().compute(x) }
    }
}