package solutions.y2023

import Solution
import utils.Utils.lcm
import utils.Utils.rl
import java.io.File

class Day8 : Solution(2023) {

    override fun solvePart1(input: File): Int {
        val moves = input.rl().first()
        val nodes = input.rl().drop(2).associate {
            val (name, neighbors) = it.split(" = ")
            val (left, right) = neighbors.removeSurrounding("(", ")").split(", ")
            name to (left to right)
        }
        var current = "AAA"
        var step = 0
        while (current != "ZZZ") {
            val node = nodes[current]!!
            current = if (moves[step % moves.length] == 'L') node.first else node.second
            step++
        }
        return step
    }

    override fun solvePart2(input: File): Long {
        val moves = input.rl().first()
        val nodes = input.rl().drop(2).associate {
            val (name, neighbors) = it.split(" = ")
            val (left, right) = neighbors.removeSurrounding("(", ")").split(", ")
            name to (left to right)
        }
        var steps = 1L
        for (i in nodes.filter { it.key.endsWith("A") }.keys) {
            var step = 0L
            var cur = i
            while (cur.endsWith("Z").not()) {
                val node = nodes[cur]!!
                cur = if (moves[step.mod(moves.length)] == 'L') node.first else node.second
                step++
            }
            steps = lcm(steps, step)
        }
        return steps
    }
}