package solutions.y2022

import Solution
import utils.Utils.extractNumbers
import java.io.File

class Day16 : Solution(2022) {

    private fun parseInput(lines: List<String>): Pair<Map<String, Int>, Map<String, List<String>>> {
        val valves = mutableMapOf<String, Int>()
        val tunnels = mutableMapOf<String, List<String>>()
        lines.forEach { l ->
            l.split(" ")[1].let {
                valves[it] = l.extractNumbers().toInt()
                tunnels[it] = l.substringAfter("valve").substringAfter(" ").split(", ")
            }
        }
        return valves to tunnels
    }

    private fun calculateDistances(
        valves: Map<String, Int>, tunnels: Map<String, List<String>>
    ): Pair<Map<String, Map<String, Int>>, List<String>> {
        val distances = mutableMapOf<String, MutableMap<String, Int>>()
        val nonEmptyValves = valves.filterKeys { it != "AA" && valves[it] != 0 }.keys.toList()
        for ((valve, flow) in valves) {
            if (valve != "AA" && flow == 0) continue
            val visited = mutableSetOf(valve)
            val queue = ArrayDeque(listOf(0 to valve))
            distances[valve] = mutableMapOf(valve to 0, "AA" to 0)
            while (queue.isNotEmpty()) {
                val (distance, position) = queue.removeFirst()
                tunnels[position]?.forEach { neighbor ->
                    if (neighbor !in visited) {
                        visited.add(neighbor)
                        if ((valves[neighbor] ?: 0) > 0) distances[valve]!![neighbor] = distance + 1
                        queue.add((distance + 1) to neighbor)
                    }
                }
            }
            distances[valve]!!.apply {
                remove(valve)
                if (valve != "AA") remove("AA")
            }
        }
        return distances to nonEmptyValves
    }

    private fun findMaxFlow(
        time: Int,
        distances: Map<String, Map<String, Int>>,
        nonEmptyValves: List<String>,
        valves: Map<String, Int>,
        bitmaskEnd: Int
    ): Int {
        val indices = nonEmptyValves.withIndex().associate { it.value to it.index }
        val cache = mutableMapOf<Triple<Int, String, Int>, Int>()
        fun dfs(time: Int, valve: String, bitmask: Int): Int {
            return cache.getOrPut(Triple(time, valve, bitmask)) {
                (distances[valve]?.keys ?: emptyList()).fold(0) { maxFlow, neighbor ->
                    val bit = 1 shl (indices[neighbor] ?: return@fold maxFlow)
                    if (bitmask and bit != 0 || time - (distances[valve]?.get(neighbor)
                            ?: return@fold maxFlow) - 1 <= 0
                    ) return@fold maxFlow
                    maxOf(
                        maxFlow,
                        dfs(time - (distances[valve]!![neighbor]!!) - 1, neighbor, bitmask or bit) + (valves[neighbor]
                            ?: 0) * (time - (distances[valve]!![neighbor]!!) - 1)
                    )
                }
            }
        }

        var maxFlow = 0
        for (i in 0..<bitmaskEnd) {
            maxFlow = maxOf(maxFlow, dfs(time, "AA", i) + dfs(time, "AA", (1 shl nonEmptyValves.size) - 1 xor i))
        }

        return maxFlow
    }

    override fun solvePart1(input: File): Any {
        val (valves, tunnels) = parseInput(input.readLines())
        val (distances, nonEmptyValves) = calculateDistances(valves, tunnels)
        return findMaxFlow(30, distances, nonEmptyValves, valves, 1)
    }

    override fun solvePart2(input: File): Any {
        val (valves, tunnels) = parseInput(input.readLines())
        val (distances, nonEmptyValves) = calculateDistances(valves, tunnels)
        return findMaxFlow(26, distances, nonEmptyValves, valves, (1 shl nonEmptyValves.size) + 1 shr 1)
    }
}
