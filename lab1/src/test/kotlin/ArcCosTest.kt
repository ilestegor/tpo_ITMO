import org.example.task1.powerSeries
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertTimeoutPreemptively
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.Duration
import kotlin.math.acos
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.test.assertEquals


class ArcCosTest {
    private val TOLERANCE = 1e-2


    @ParameterizedTest
    @DisplayName("Corner cases")
    @ValueSource(doubles = [1.0, -1.0, 0.0])
    fun `Cornet cases`(x: Double) {
        Assertions.assertEquals(powerSeries(x), acos(x))

    }


    @ParameterizedTest
    @DisplayName("Invalid inputs")
    @ValueSource(doubles = [1.1, -1.1])
    fun `Invalid inputs`(x: Double) {
        assertThrows<IllegalArgumentException> { powerSeries(x) }
    }


    @ParameterizedTest
    @DisplayName("Base x-values")
    @ValueSource(doubles = [0.5, -0.5])
    fun `Base x-values`(x: Double) {
        assertEquals(powerSeries(x), acos(x), TOLERANCE)
    }

}