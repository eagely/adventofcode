package solutions.y2023

import Solution
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph
import utils.Utils.rl
import utils.Utils.split
import java.io.File

class Day25 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        input.rl().forEach {
            val nodes = it.split(":")
            val root = nodes.first()
            graph.addVertex(root)
            for (i in nodes.last().split()) {
                graph.addVertex(i)
                graph.addEdge(root, i)
            }
        }
        val minCut = StoerWagnerMinimumCut(graph).minCut()
        graph.removeAllVertices(minCut)
        return graph.vertexSet().size * minCut.size
    }

    override fun solvePart2(input: File): Any {
        return "me when the big red button"
    }
}
