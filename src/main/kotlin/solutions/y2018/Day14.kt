package solutions.y2018

import Solution
import utils.join
import utils.text
import java.io.File

class Day14 : Solution(2018) {

    override fun solvePart1(input: File): Any {
        val req = input.text.toInt()
        val scoreboard = mutableListOf(3, 7)
        var elfA = 0
        var elfB = 1
        var count = 0

        while (count <= req + 10) {
            val recipeA = scoreboard[elfA]
            val recipeB = scoreboard[elfB]
            scoreboard.addAll((recipeA + recipeB).toString().toList().map { it.digitToInt() })
            elfA = (elfA + recipeA + 1) % scoreboard.size
            elfB = (elfB + recipeB + 1) % scoreboard.size
            count++
        }

        return scoreboard.subList(req, req + 10).join()
    }

    override fun solvePart2(input: File): Any {
        val req = input.text.toList().map { it.digitToInt() }
        val scoreboard = mutableListOf(3, 7)
        var elfA = 0
        var elfB = 1
        var count = 0

        while (count - req.size !in scoreboard.indices || scoreboard.subList(count - req.size, count) != req) {
            val recipeA = scoreboard[elfA]
            val recipeB = scoreboard[elfB]
            scoreboard.addAll((recipeA + recipeB).toString().toList().map { it.digitToInt() })
            elfA = (elfA + recipeA + 1) % scoreboard.size
            elfB = (elfB + recipeB + 1) % scoreboard.size
            count++
        }

        return count - req.size
    }
}