package UnitTest

import org.example.logarithmicFunctions.LogBase
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.log
import kotlin.test.assertEquals

class LogBaseTest {
    private val EPSILON: Double = 1e-7

    companion object {


        private val log2BaseMockData: MutableMap<Double, Double> = mutableMapOf()
        private val log3BaseMockData: MutableMap<Double, Double> = mutableMapOf()


        private fun loadDataIntoMap(files: List<String>, targetMap: MutableMap<Double, Double>) {
            files.forEach { file ->
                Files.readAllLines(Paths.get("src/test/resources$file"))
                    .map { line ->
                        val (x, y) = line.split(",").map { it.trim().toDouble() }
                        x to y // Create a Pair for key-value mapping
                    }
                    .forEach { (x, y) ->
                        targetMap[x] = y // Store x as key, y as value
                    }
            }
        }

        init {

            val log2BaseFiles = listOf("/log2_mock_values.csv")
            loadDataIntoMap(log2BaseFiles, log2BaseMockData)

            val log3BaseFiles = listOf("/log3_mock_values.csv")
            loadDataIntoMap(log3BaseFiles, log3BaseMockData)


        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun shouldThrowExceptionOnInvalidX(x: Double) {
        assertThrows<IllegalArgumentException> { LogBase(2.0).compute(x) }
    }

    @ParameterizedTest
    @ValueSource(doubles = [1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY])
    fun shouldThrowExceptionOnInvalidBase(x: Double) {
        assertThrows<IllegalArgumentException> { LogBase(x).compute(4.0) }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.4, 1.0, 4.0])
    fun shouldCalculateLogForXValues(x: Double) {
        val logBase3Mock = Mockito.mock(LogBase::class.java)

        whenever(logBase3Mock.compute(x)).thenReturn(log3BaseMockData[x])
        assertEquals(logBase3Mock.compute(x), LogBase(3.0).compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [2.0, 6.0, 10.0])
    fun shouldCalculateLogWithBases(x: Double) {
        assertEquals(log(2.0, x), LogBase(x).compute(2.0), EPSILON)
    }
}