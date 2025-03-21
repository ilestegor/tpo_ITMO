package UnitTest

import org.example.logarithmicFunctions.Ln
import org.example.logarithmicFunctions.LogBase
import org.example.system.FunctionSystem
import org.example.trigonometricFunctions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI
import kotlin.math.ln
import kotlin.math.pow

class FunctionSystemUnitTest {
    private val EPSILON: Double = 1e-7

    companion object {

        private val sinMockData: MutableMap<Double, Double> = mutableMapOf()
        private val cosMockData: MutableMap<Double, Double> = mutableMapOf()
        private val tanMockData: MutableMap<Double, Double> = mutableMapOf()
        private val cotMockData: MutableMap<Double, Double> = mutableMapOf()
        private val cscMockData: MutableMap<Double, Double> = mutableMapOf()
        private val secMockData: MutableMap<Double, Double> = mutableMapOf()
        private val lnMockData: MutableMap<Double, Double> = mutableMapOf()

        private val log2BaseMockData: MutableMap<Double, Double> = mutableMapOf()
        private val log3BaseMockData: MutableMap<Double, Double> = mutableMapOf()

        private val log5BaseMockData: MutableMap<Double, Double> = mutableMapOf()


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

            val cosFiles = listOf("/cos_mock_value.csv")
            loadDataIntoMap(cosFiles, cosMockData)

            val tanFile = listOf("/tan_mock_values.csv")
            loadDataIntoMap(tanFile, tanMockData)

            val cotFiles = listOf("/cot_mock_values.csv")
            loadDataIntoMap(cotFiles, cotMockData)

            val cscFiles = listOf("/csc_mock_values.csv")
            loadDataIntoMap(cscFiles, cscMockData)

            val secFiles = listOf("/sec_mock_values.csv")
            loadDataIntoMap(secFiles, secMockData)

            val lnFiles = listOf("/ln_mock_values.csv")
            loadDataIntoMap(lnFiles, lnMockData)

            val log2BaseFiles = listOf("/log2_mock_values.csv")
            loadDataIntoMap(log2BaseFiles, log2BaseMockData)

            val log3BaseFiles = listOf("/log3_mock_values.csv")
            loadDataIntoMap(log3BaseFiles, log3BaseMockData)

            val log5BaseFiles = listOf("/log5_mock_values.csv")
            loadDataIntoMap(log5BaseFiles, log5BaseMockData)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-(3 * PI / 2), -PI, -2 * PI, -PI / 2, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN])
    fun `should throw Exception when x less or equals 0`(x: Double) {
        assertThrows<IllegalArgumentException> { FunctionSystem().compute(x) }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-PI / 7, -0.48957, -PI / 6, -0.57362, -0.56462, -0.65094, -0.59, -1.02533, -PI / 4, -1.34, -0.6 * PI, -0.7 * PI, -2.08916, -0.85 * PI, -0.95 * PI, -2.87357, -1.1 * PI, -1.3 * PI, -3.75882, -1.6 * PI, -1.8 * PI, -5.37171 * PI])
    fun shouldCalculateTrigonometric(x: Double) {
        val sinMock = Mockito.mock(Sin::class.java)
        val cosMock = Mockito.mock(Cos::class.java)
        val tanMock = Mockito.mock(Tg::class.java)
        val cotMock = Mockito.mock(Ctg::class.java)
        val cscMock = Mockito.mock(Csc::class.java)
        val secMock = Mockito.mock(Sec::class.java)

        whenever(sinMock.compute(x)).thenReturn(sinMockData[x])
        whenever(cosMock.compute(x)).thenReturn(cosMockData[x])
        whenever(tanMock.compute(x)).thenReturn(tanMockData[x])
        whenever(cotMock.compute(x)).thenReturn(cotMockData[x])
        whenever(cscMock.compute(x)).thenReturn(cscMockData[x])
        whenever(secMock.compute(x)).thenReturn(secMockData[x])
        val expected =
            (((((((1 / cosMock.compute(x)) / cosMock.compute(x)).pow(3)) - (sinMock.compute(x) - (1 / tanMock.compute(x)))) + (1 / sinMock.compute(
                x
            ))) * ((1 / tanMock.compute(x)) / (1 / cosMock.compute(x)))) / ((tanMock.compute(x) + cosMock.compute(x)) + (tanMock.compute(
                x
            )
                .pow(3))))

        assertEquals(expected, FunctionSystem().compute(x), EPSILON)
    }

    @ParameterizedTest
    @ValueSource(doubles = [1.0])
    fun shouldThrowExceptionForLogarithmic(x: Double) {
        assertThrows<IllegalArgumentException> { FunctionSystem().compute(x) }
    }


    @ParameterizedTest
    @ValueSource(doubles = [0.001, 0.19196, 0.26703, 0.5, 2.0])
    fun shouldCalculateLogarithmic(x: Double) {
        val log2BaseMock = Mockito.mock(LogBase::class.java)
        val log3BaseMock = Mockito.mock(LogBase::class.java)
        val log5BaseMock = Mockito.mock(LogBase::class.java)
        val lnMock = Mockito.mock(Ln::class.java)

        whenever(log2BaseMock.compute(x)).thenAnswer { log2BaseMockData[x] }
        whenever(log3BaseMock.compute(x)).thenReturn(log3BaseMockData[x])
        whenever(log5BaseMock.compute(x)).thenReturn(log5BaseMockData[x])
        whenever(lnMock.compute(x)).thenReturn(lnMockData[x])

        val expected =
            (((((log5BaseMock.compute(x) + log3BaseMock.compute(x)).pow(2)) * log3BaseMock.compute(x)) + (log2BaseMock.compute(
                x
            ) * (log2BaseMock.compute(x) + ln(x)))) * ((log3BaseMock.compute(x)
                .pow(3)) / (log3BaseMock.compute(x) - log2BaseMock.compute(x))))
        assertEquals(expected, FunctionSystem().compute(x), EPSILON)
    }


}