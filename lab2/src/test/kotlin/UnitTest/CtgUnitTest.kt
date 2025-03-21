package UnitTest

import org.example.trigonometricFunctions.Ctg
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI
import kotlin.test.assertEquals

class CtgUnitTest {

    private val EPSILON = 1e-7

    companion object {

        private val ctgMockData: MutableMap<Double, Double> = mutableMapOf()


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
            val ctgFiles = listOf("/cot_mock_values.csv")
            loadDataIntoMap(ctgFiles, ctgMockData)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [PI / 6, 0.75 * PI])
    fun shouldCalculateCot(x: Double) {
        val cotMock = Mockito.mock(Ctg::class.java)

        whenever(cotMock.compute(x)).thenReturn(ctgMockData[x])
        assertEquals(cotMock.compute(x), Ctg().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, PI, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun invalidInputTest(x: Double) {
        assertThrows<IllegalArgumentException> { Ctg().compute(x) }
    }

}