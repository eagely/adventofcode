package solutions.y2024

import Solution
import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import utils.combinations
import utils.join
import utils.lines
import java.io.File

class Day23 : Solution(2024) {

    override fun solvePart1(input: File) = BronKerboschCliqueFinder(parse(input)).iterator().asSequence()
        .flatMap { if (it.size >= 3) it.toList().combinations(3) else emptyList() }
        .filter { c -> c.any { it.startsWith('t') } }.toSet().size

    override fun solvePart2(input: File) = BronKerboschCliqueFinder(parse(input)).maxBy { it.size }.sorted().join(",")

    private fun parse(input: File): SimpleGraph<String, DefaultEdge> {
        val graph = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java)
        input.lines.forEach {
            val (a, b) = it.split("-")
            graph.addVertex(a)
            graph.addVertex(b)
            graph.addEdge(a, b)
        }
        return graph
    }
}