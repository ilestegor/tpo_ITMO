import org.example.task2.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText
import kotlin.test.assertEquals


class DijkstraAlgorithmTest {


    companion object {
        val simpleGraph: Map<Int, Map<Int, Int>> by lazy {
            val g = object {}.javaClass.getResource("graph.txt")?.readText()?.split('\n')
            readGraph(g!!)
        }
    }

    @Test
    fun testEmptyFile() {
        val fp = kotlin.io.path.createTempFile()
        val expected = emptyMap<Int, Map<Int, Int>>()
        val res = readGraphFromFile(fp.toString())
        assertEquals(expected, res)
        fp.deleteIfExists()
    }

    @Test
    fun testFileNotFound() {
        val fp = "/nonexistent/file.txt"
        assertThrows<IllegalArgumentException> { readGraphFromFile(fp) }
    }


    @Test
    fun testValidGraphAndValidFile() {
        val s = kotlin.io.path.createTempFile()
        s.writeText(
            "0 1 5\n" +
                    "1 2 10\n" +
                    "2 3 15"
        )

        val expected = mapOf(
            0 to mapOf(1 to 5),
            1 to mapOf(0 to 5, 2 to 10),
            2 to mapOf(1 to 10, 3 to 15),
            3 to mapOf(2 to 15)
        )
        val graph = readGraphFromFile(s.toString())
        assertEquals(expected, graph)
        s.deleteIfExists()

    }

    @Test
    fun negativeWeightTest() {
        val s = listOf("0 1 -2")
        assertThrows<IllegalArgumentException> { readGraph(s) }
    }

    @Test
    fun notValidInputTest() {
        val s = listOf("1 a 3")
        assertThrows<NumberFormatException> { readGraph(s) }
    }

    @Test
    fun singleNodeTest() {
        val s = listOf("8")
        val expected = mapOf(8 to emptyMap<Int, Int>())
        val graph = readGraph(s)
        assertEquals(expected, graph)
    }

    @Test
    fun singleNodeFormatTest() {
        val s = listOf("a")
        assertThrows<NumberFormatException> { readGraph(s) }
    }

    @Test
    fun unconnectedNodeTest() {
        val s = listOf("8", "0 1 3")
        val expected = mapOf(8 to emptyMap(), 0 to mapOf(1 to 3), 1 to mapOf(0 to 3))
        val graph = readGraph(s)
        assertEquals(expected, graph)
    }

    @Test
    fun testSimpleGraph() {
        val result = dijkstra(simpleGraph, 0)
        val expected = mapOf(
            0 to 0,
            1 to 7,
            2 to 7,
            3 to 5,
            4 to 10,
            5 to 6,
            6 to 13,
            7 to 9,
            8 to Int.MAX_VALUE
        )

        assertEquals(expected, result.second)
    }

    @Test
    fun testDistancesFromDisconnectedVertex() {
        val result = dijkstra(simpleGraph, 8)
        val expected = mapOf(
            0 to Int.MAX_VALUE,
            1 to Int.MAX_VALUE,
            2 to Int.MAX_VALUE,
            3 to Int.MAX_VALUE,
            4 to Int.MAX_VALUE,
            5 to Int.MAX_VALUE,
            6 to Int.MAX_VALUE,
            7 to Int.MAX_VALUE,
            8 to 0
        )
        assertEquals(expected, result.second)
    }

    @Test
    fun testDijkstraWithNegativeWeight() {
        val simpleGraphWithNegativeWeight = mapOf(
            0 to mapOf(1 to -1), 1 to mapOf(0 to -1), 1 to mapOf(2 to 4), 2 to mapOf(1 to 4)
        )

        assertThrows<IllegalArgumentException> { dijkstra(simpleGraphWithNegativeWeight, 0) }
    }

    @Test
    fun testNonExistentStartVertex() {
        assertThrows<IllegalArgumentException> { dijkstra(simpleGraph, 9) }
    }

    @Test
    fun testSingleNodeGraph() {
        val startVertex = 8
        val graph: Map<Int, Map<Int, Int>> = mapOf(startVertex to emptyMap())
        val expected = 0
        val result = dijkstra(graph, startVertex).second
        assertEquals(expected, result[startVertex])
    }

    @Test
    fun emptyGraphTest() {
        val graph: Map<Int, Map<Int, Int>> = emptyMap()
        assertThrows<GraphEmptyException> { dijkstra(graph) }
    }


    @Test
    fun validGraphShortestPathToTargetVertexTest() {
        val predecessors = mapOf(
            2 to 0,
            3 to 0,
            1 to 5,
            5 to 3,
            7 to 3,
            6 to 7,
            4 to 7
        )
        val expected = listOf(0, 3, 7, 4)
        val targetVertex = 4
        val result = getShortestPath(predecessors, targetVertex)
        assertEquals(expected, result)
    }

    @Test
    fun singleVertxPathTest() {
        val predecessors: Map<Int, Int> = emptyMap()
        val expected = listOf(-1)
        val result = getShortestPath(predecessors, 3)
        assertEquals(expected, result)
    }

    @Test
    fun testNonExistentTargetPath() {
        val predecessors = mapOf(
            2 to 1,
            3 to 1,
            4 to 2
        )

        val expected = listOf(-1)
        val targetVertex = 5
        val result = getShortestPath(predecessors, targetVertex)
        assertEquals(expected, result)
    }

}