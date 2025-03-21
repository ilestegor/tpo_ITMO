package IntegrationTest

import org.example.logarithmicFunctions.Ln
import org.example.logarithmicFunctions.LogBase
import org.example.system.FunctionSystem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths

class FunctionSystemLogarithmicIntegrationTest {
    private val EPSILON: Double = 1e-7

    companion object {

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
    @CsvFileSource(resources = ["/log_angle_fs_output.csv"])
    fun functionSystemLogAllMocks(x: Double, y: Double) {
        val log2BaseMock = Mockito.mock(LogBase::class.java)
        val log3BaseMock = Mockito.mock(LogBase::class.java)
        val log5BaseMock = Mockito.mock(LogBase::class.java)
        val lnMock = Mockito.mock(Ln::class.java)

        whenever(log2BaseMock.compute(x)).thenAnswer { log2BaseMockData[x] }
        whenever(log3BaseMock.compute(x)).thenReturn(log3BaseMockData[x])
        whenever(log5BaseMock.compute(x)).thenReturn(log5BaseMockData[x])
        whenever(lnMock.compute(x)).thenReturn(lnMockData[x])

        val result = FunctionSystem(
            logBase2 = log2BaseMock,
            logBase3 = log3BaseMock,
            logBase5 = log5BaseMock,
            ln = lnMock
        ).compute(x)

        assertEquals(y, result, EPSILON)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/log_angle_fs_output.csv"])
    fun functionSystemLogLnMock(x: Double, y: Double) {
        val lnMock = Mockito.mock(Ln::class.java)

        val log2Base = LogBase(2.0, lnMock)
        val log3Base = LogBase(3.0, lnMock)
        val log5Base = LogBase(5.0, lnMock)

        whenever(lnMock.compute(any(), any())).thenAnswer { invocation ->
            val argX = invocation.arguments[0] as Double
            lnMockData[argX]
        }

        val result =
            FunctionSystem(logBase2 = log2Base, logBase3 = log3Base, logBase5 = log5Base, ln = lnMock).compute(x)

        assertEquals(y, result, EPSILON)

    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/log_angle_fs_output.csv"])
    fun functionSystemLogAllReal(x: Double, y: Double) {
        val result = FunctionSystem().compute(x)

        assertEquals(y, result, EPSILON)
    }
}