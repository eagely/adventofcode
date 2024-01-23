package solutions.y2021

import Solution
import utils.Utils.allEquals
import utils.Utils.die
import utils.Utils.sdnl
import utils.Utils.split
import utils.Utils.trimMultiSpace
import utils.grid.Grid
import java.io.File

class Day4 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val lines = input.sdnl()
        val rolls = lines.first().split(",")
        val boards = lines.drop(1).map { Grid.of(it.split("\n").map { it.trimMultiSpace().trim().split() }) }

        rolls.forEach { roll ->
            boards.forEach { board ->
                if (roll in board)
                    board[board[roll]!!] = "X"
                if (board.getColumns().any { it.allEquals("X") } || board.getRows().any { it.allEquals("X") })
                    return roll.toInt() * board.toList().filter { it != "X" }.sumOf { it.toInt() }
            }
        }
        die()
    }

    override fun solvePart2(input: File): Any {
        val lines = input.sdnl()
        val rolls = lines.first().split(",")
        val boards = lines.drop(1).map { Grid.of(it.split("\n").map { it.trimMultiSpace().trim().split() }) }

        rolls.forEach { roll ->
            boards.forEach { board ->
                if (roll in board)
                    board[board[roll]!!] = "X"
                if (boards.all { b -> b.getColumns().any { it.allEquals("X") } || b.getRows().any { it.allEquals("X") } })
                    return roll.toInt() * board.toList().filter { it != "X" }.sumOf { it.toInt() }
            }
        }
        die()
    }
}
