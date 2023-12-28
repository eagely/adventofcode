package solutions.y2020

import Solution
import utils.Utils.rl
import utils.grid.Grid
import utils.grid.Grid.Companion.gameOfLife
import java.io.File

class Day11 : Solution(2020) {

    override fun solvePart1(input: File) = Grid.of(input.rl()).gameOfLife { p, c, snap -> if (c == 'L' && '#' !in snap.getNeighbors(p)) '#' else if (c == '#' && snap.getNeighbors(p).count { it == '#' } >= 4) 'L' else c } .count { it == '#' }

    override fun solvePart2(input: File) = Grid.of(input.rl()).gameOfLife { p, c, snap -> if (c == 'L' && '#' !in snap.getNeighbors(p, '.')) '#' else if (c == '#' && snap.getNeighbors(p, '.').count { it == '#' } >= 5) 'L' else c }.count { it == '#' }
}