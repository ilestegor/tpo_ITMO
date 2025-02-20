import org.example.task1.powerSeries
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertTimeoutPreemptively
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.Duration
import kotlin.math.acos
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.test.assertEquals


class ArcCosTest {
    private val TOLERANCE = 1e-5


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
        assertTimeoutPreemptively(Duration.ofSeconds(2)) {
            assertThrows<IllegalArgumentException> { powerSeries(x) }
        }
    }

    companion object {
        private val SEED = 42

        @JvmStatic
        fun computedValues(): List<Double> {
            return listOf(-(sqrt(3.0) / 2), -(sqrt(2.0) / 2), (sqrt(3.0) / 2), (sqrt(2.0) / 2))
        }

        @JvmStatic
        fun randomValues(): List<Double> {
            val random = Random(SEED)
            return List(15) {
                random.nextDouble(-1.0, 1.0)
            }
        }
    }

    @ParameterizedTest
    @DisplayName("Base x-values")
    @ValueSource(doubles = [0.5, -0.5])
    @MethodSource("computedValues")
    fun `Base x-values`(x: Double) {
        assertEquals(powerSeries(x), acos(x), TOLERANCE)
    }

    @ParameterizedTest
    @DisplayName("Random values from -1 to 1")
    @MethodSource("randomValues")
    fun `Random Values Test`(x: Double) {
        assertEquals(powerSeries(x), acos(x), TOLERANCE)
    }


}