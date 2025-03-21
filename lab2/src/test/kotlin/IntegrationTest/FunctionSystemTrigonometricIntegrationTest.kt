package IntegrationTest

import org.example.system.FunctionSystem
import org.example.trigonometricFunctions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.nio.file.Files
import java.nio.file.Paths

class FunctionSystemTrigonometricIntegrationTest {
    private val EPSILON: Double = 1e-7


    companion object {

        private val sinMockData: MutableMap<Double, Double> = mutableMapOf()
        private val cosMockData: MutableMap<Double, Double> = mutableMapOf()
        private val tanMockData: MutableMap<Double, Double> = mutableMapOf()
        private val cotMockData: MutableMap<Double, Double> = mutableMapOf()
        private val cscMockData: MutableMap<Double, Double> = mutableMapOf()
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


        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/trig_angle_fs_output.csv"])
    fun functionSystemTrigonometricAllMocks(x: Double, y: Double) {
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

        val result: Double = FunctionSystem(sinMock, cosMock, tanMock, cotMock, secMock, cscMock).compute(x)

        assertEquals(y, result, EPSILON)

    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/trig_angle_fs_output.csv"])
    fun functionSystemTrigonometricSecondLevel(x: Double, y: Double) {
        val cosMock = Mockito.mock(Cos::class.java)
        val cscMock = Mockito.mock(Csc::class.java)
        val sinMock = Mockito.mock(Sin::class.java)

        val tan = Tg(sinMock, cosMock)
        val cot = Ctg(sinMock, cosMock)
        val sec = Sec(cosMock)

        whenever(cosMock.compute(x)).thenReturn(cosMockData[x])
        whenever(cscMock.compute(x)).thenReturn(cscMockData[x])
        whenever(sinMock.compute(x)).thenReturn(sinMockData[x])

        val result: Double = FunctionSystem(sinMock, cosMock, tan, cot, sec, cscMock).compute(x)
        assertEquals(y, result, EPSILON)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/trig_angle_fs_output.csv"])
    fun functionSystemTrigonometricThirdLevel(x: Double, y: Double) {
        val sinMock = Mockito.mock(Sin::class.java)

        val cos = Cos(sinMock)
        val csc = Csc(sinMock)
        val tan = Tg(sinMock, cos)
        val cot = Ctg(sinMock, cos)
        val sec = Sec(cos)

        whenever(sinMock.compute(any(), any())).thenAnswer { invocation ->
            val argX = invocation.arguments[0] as Double
            sinMockData[argX]
        }


        val result: Double = FunctionSystem(sinMock, cos, tan, cot, sec, csc).compute(x)
        assertEquals(y, result, EPSILON)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/trig_angle_fs_output.csv"])
    fun functionSystemTrigonometricFourthLevel(x: Double, y: Double) {
        val result: Double = FunctionSystem().compute(x)

        assertEquals(y, result, EPSILON)
    }
}