package solutions.y2021

import Solution
import utils.Utils.asInt
import utils.Utils.p
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.PathFinding
import java.io.File

class Day15 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val graph = Grid.of(input.rl()).map { it.asInt() }
        return PathFinding.dijkstraCost(graph.minX p graph.minY, graph.maxX p graph.maxY) { graph.getCardinalNeighborPositions(it).map { PathFinding.Weighted(it, graph[it]!!) } }
    }

    override fun solvePart2(input: File): Any {
        val graph = Grid.of(input.rl()).map { it.asInt() }
        val size = graph.rows
        return PathFinding.dijkstraCost(graph.minX p graph.minY, (graph.maxX + 1) * 5 - 1 p (graph.maxY + 1) * 5 - 1) { it.getCardinalNeighbors().filter { it.x in 0..<size*5 && it.y in 0..<size*5 }.map { PathFinding.Weighted(it, (graph[it % size]!! + it.y / size + it.x / size - 1) % 9 + 1) } }
    }
}