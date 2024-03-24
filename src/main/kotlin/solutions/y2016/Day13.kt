package solutions.y2016

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest
import utils.movement.PathFinding
import utils.point.Point

class Day13 : Solution(2016) {

    @NoTest
    override fun solvePart1(input: File): Any {
        val fav = input.text.toInt()
        return PathFinding.bfsPath(1 p 1, 31 p 39) { it.next(fav) }.length
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        val fav = input.text.toInt()
        val paths = PathFinding.bfsAllPaths(1 p 1) { it.next(fav) }
        return (0..50).sumOf { x -> (0..50).count { y -> paths.getPath(x p y).length in 0..50 } } + 1
    }

    private fun Point.next(fav: Int) = getCardinalNeighbors().filter { (x, y) -> x >= 0 && y >= 0 && (x*x + 3*x + 2*x*y + y + y*y + fav).toString(2).count { it == '1' } % 2 == 0 }
}