package UnitTest

import org.example.trigonometricFunctions.Cos
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI

class CosUnitTest {
    private val EPSILON: Double = 1e-7

    companion object {

        private val cosMockData: MutableMap<Double, Double> = mutableMapOf()


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
            val cosFiles = listOf("/cos_mock_value.csv")
            loadDataIntoMap(cosFiles, cosMockData)
        }
    }


    @ParameterizedTest
    @ValueSource(doubles = [0.0, PI / 2, PI, 1.5 * PI, 2 * PI])
    fun cornerCasesForCos(x: Double) {
        val cosMock = Mockito.mock(Cos::class.java)

        whenever(cosMock.compute(x)).thenReturn(cosMockData[x])
        assertEquals(cosMock.compute(x), Cos().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [PI / 6, (3 * PI) / 4, 1.2 * PI, 1.7 * PI])
    fun equivalentCasesForCos(x: Double) {
        val cosMock = Mockito.mock(Cos::class.java)
        whenever(cosMock.compute(x)).thenReturn(cosMockData[x])
        assertEquals(cosMock.compute(x), Cos().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun invalidInputTest(x: Double) {
        assertThrows<IllegalArgumentException> { Cos().compute(x) }
    }

}