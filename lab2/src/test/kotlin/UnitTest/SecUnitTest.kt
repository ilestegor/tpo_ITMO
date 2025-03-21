package UnitTest

import org.example.trigonometricFunctions.Csc
import org.example.trigonometricFunctions.Sec
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI
import kotlin.test.assertEquals

class SecUnitTest {
    private val EPSILON: Double = 1e-7

    companion object {

        private val secMockData: MutableMap<Double, Double> = mutableMapOf()


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
            val secFiles = listOf("/sec_mock_values.csv")
            loadDataIntoMap(secFiles, secMockData)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-PI, 0.0, PI])
    fun shouldComputeAtKeyPoints(x: Double) {
        val secMock = Mockito.mock(Csc::class.java)

        whenever(secMock.compute(x)).thenReturn(secMockData[x])
        assertEquals(secMock.compute(x), Sec().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [-0.3 * PI, 0.3 * PI, 0.8 * PI, 1.2 * PI])
    fun shouldCalculateSec(x: Double) {
        val secMock = Mockito.mock(Csc::class.java)

        whenever(secMock.compute(x)).thenReturn(secMockData[x])
        assertEquals(secMock.compute(x), Sec().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [-PI / 2, PI / 2, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun invalidInputTest(x: Double) {
        assertThrows<IllegalArgumentException> { Sec().compute(x) }
    }
}