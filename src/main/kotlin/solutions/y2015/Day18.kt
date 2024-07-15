package solutions.y2015

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest
import utils.grid.Grid
import utils.grid.Grid.Companion.gameOfLife

class Day18 : Solution(2015) {

    override fun solvePart1(input: File) = Grid.of(input.lines.map { it.map { it == '#' }}).gameOfLife(100) { p, it, g -> g.getNeighbors (p).count { it == true }.let { n -> (it == true && (n == 2 || n == 3)) || (it != true && n == 3)} }.count { it }

    @NoTest
    override fun solvePart2(input: File): Any {
        val corners = setOf(0 p 0, 0 p 99, 99 p 0, 99 p 99)
        return Grid.of(input.lines.map { it.map { it == '#' }}).apply { set(corners, true) }.gameOfLife(100) { p, it, g -> g.getNeighbors (p).count { it == true }.let { n -> (p in corners) || (it == true && (n == 2 || n == 3)) || (it != true && n == 3)} }.count { it }
    }
}