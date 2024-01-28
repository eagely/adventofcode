package solutions.y2021

import Solution
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import utils.Utils.isLowercase
import utils.Utils.isUppercase
import utils.Utils.rl
import utils.graph.GraphUtils.getConnection
import java.io.File
import java.util.*

class Day12 : Solution(2021) {

    override fun solvePart1(input: File) = buildGraph(input).getPaths{ t, cur -> (t.isLowercase() && t !in cur) || t.isUppercase() }.size

    override fun solvePart2(input: File): Any {
        val graph = buildGraph(input)
        return graph.vertexSet().filter { it != "start" && it != "end" && it.isLowercase() }.flatMap { graph.getPaths { t, cur -> (t.isLowercase() && t !in cur) || t.isUppercase() || (t == it && cur.count { it == t } < 2) } }.distinct().size
    }

    private fun SimpleGraph<String, DefaultEdge>.getPaths(condition: (String, List<String>) -> Boolean): List<List<String>> {
        val paths = mutableListOf<List<String>>()
        val stack = Stack<List<String>>()
        stack.push(listOf("start"))
        while (stack.isNotEmpty()) {
            val cur = stack.pop()
            if (cur.last() == "end") {
                paths.add(cur)
                continue
            }
            for (e in this.edgesOf(cur.last())) {
                val t = this.getConnection(e, cur.last())
                if (condition(t, cur)) stack.push(cur + t)
            }
        }
        return paths
    }

    private fun buildGraph(input: File): SimpleGraph<String, DefaultEdge> {
        val graph = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java)
        for (line in input.rl()) {
            val (a, b) = line.split('-')
            graph.addVertex(a)
            graph.addVertex(b)
            graph.addEdge(a, b)
        }
        return graph
    }
}