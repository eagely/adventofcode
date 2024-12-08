package solutions.y2024

import Solution
import utils.chargrid
import utils.grid.Grid
import utils.point.Point
import utils.zipWithAllIfItsNonAssociative
import java.io.File

class Day8 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val grid = input.chargrid()

        return grid.sumPairings { a: Point, b: Point ->
            Point(a.x + a.x - b.x, a.y + a.y - b.y).takeIf { it in grid }?.let { sequenceOf(it) } ?: emptySequence()
        }
    }

    override fun solvePart2(input: File): Any {
        val grid = input.chargrid()
        return grid.sumPairings { a: Point, b: Point ->
            generateSequence(Point(a.x, a.y)) { prev ->
                Point(prev.x + a.x - b.x, prev.y + a.y - b.y).takeIf { it in grid }
            }
        }
    }

    private fun Grid<Char>.sumPairings(whatToSum: (Point, Point) -> Sequence<Point>) =
        data.keys.filter { this[it]!!.isLetterOrDigit() }.groupBy { this[it]!!.code }.flatMap { (_, positions) ->
            positions.zipWithAllIfItsNonAssociative().flatMap { (a, b) ->
                whatToSum(a, b).toList().takeIf { it.isNotEmpty() } ?: emptyList()
            }
        }.distinct().size
}
