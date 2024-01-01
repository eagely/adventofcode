package solutions.y2020

import Solution
import utils.Utils.rl
import utils.grid.ArbitraryGrid
import utils.grid.ArbitraryGrid.Companion.gameOfLife
import java.io.File

class Day17 : Solution(2020) {

    override fun solvePart1(input: File) = gameOfLife(input, 3)

    override fun solvePart2(input: File) = gameOfLife(input, 4)

    private fun gameOfLife(input: File, dimensions: Int) = ArbitraryGrid.of(input.rl(), dimensions).gameOfLife(6) { p, c, g -> g.getNeighbors(p).count { it == '#' }.let { if (c == '#' && it in 2..3) '#' else if (c != '#' && it == 3) '#' else '.' } }.count { it == '#' }
}