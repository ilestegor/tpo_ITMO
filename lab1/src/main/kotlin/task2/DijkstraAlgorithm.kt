package org.example.task2


import java.io.File
import java.io.FileNotFoundException
import java.util.*

tailrec fun readGraphFromFile(filePath: String): Map<Int, Map<Int, Int>> {
    return try {
        readGraph(File(filePath).readLines())
    } catch (e: FileNotFoundException){
        throw IllegalArgumentException("Graph file not found: $filePath")
    }
}

fun readGraph(lines: List<String>): Map<Int, Map<Int, Int>> {
    val graph = mutableMapOf<Int, MutableMap<Int, Int>>()

    lines.mapNotNull { line ->
        val parts = line.split(" ")
        try {
            when (parts.size) {
                3 -> {
                    val (v1, v2, weight) = parts.map { it.toInt() }
                    require(weight >= 0) { "Weight can't be less than zero" }
                    graph.getOrPut(v1) { mutableMapOf() }[v2] = weight
                    graph.getOrPut(v2) { mutableMapOf() }[v1] = weight
                }
                1 -> {
                    val v1 = parts[0].toInt()
                    graph.getOrPut(v1) { mutableMapOf() }
                }
                else -> return@mapNotNull null
            }
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Only integers allowed")
        }
    }

    return graph
}

fun dijkstra(graph: Map<Int, Map<Int, Int>>, startVertex: Int = 0): Pair<Map<Int, Int>, Map<Int, Int>> {
    if (graph.isEmpty()) throw GraphEmptyException("Graph is empty")
    require(startVertex in graph) {"Start vertex $startVertex does not exist"}

    val distances = graph.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
    val predecessor = mutableMapOf<Int, Int>()
    val pq =PriorityQueue(compareBy<Pair<Int, Int>>{it.second})


    pq.add(startVertex to 0)
    distances[startVertex] = 0

    while (pq.isNotEmpty()) {
        // Get the node with the smallest distance
        val (currentNode, currentDist) = pq.poll()

        // Skip if the current node's distance is already greater than the known shortest distance
        if (currentDist > distances[currentNode]!!) continue

        graph[currentNode]?.forEach {(neighbor, weight) ->
            require(weight >0) {"Graph contains negative weight: $weight"}
            val newDist = currentDist + weight
            if (newDist < distances[neighbor]!!) {
                distances[neighbor] = newDist
                predecessor[neighbor] = currentNode
                pq.add(Pair(neighbor, newDist))
            }
        }
    }

    return predecessor to distances
}

fun getShortestPath(predecessor: Map<Int, Int>, target: Int): List<Int> {
    if (predecessor.isEmpty())
        return listOf(-1)
    val path = mutableListOf<Int>()

    var curNode = target
    if (!predecessor.containsKey(curNode))
        return listOf(-1)
    while (curNode in predecessor) {
        path.add(curNode)
        curNode = predecessor[curNode]!!
    }
    path.add(curNode)
    return path.reversed()
}