package solutions.y2019

import Solution
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.computing.IntCode
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import utils.text
import java.io.File

class Day11 : Solution(2019) {

    override fun solvePart1(input: File) = solve(input, false).first

    override fun solvePart2(input: File) = solve(input, true).second

    private fun solve(input: File, firstTileWhite: Boolean): Pair<Int, Grid<Char>> {
        val program = IntCode(input.text.split(',').withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap().withDefault { 0 }, Channel(Channel.UNLIMITED))
        val seen = HashSet<Point>()
        val painted = HashSet<Point>()
        var pos = Point(0, 0)
        var dir = Direction.NORTH
        if (firstTileWhite)
            seen.add(pos)
        runBlocking {
            launch { program.runSuspending() }
            while (!program.finished) {
                program.input.send(if (pos in seen) 1L else 0L)
                when (program.output.receive()) {
                    0L -> seen.remove(pos)
                    1L -> seen.add(pos)
                }
                when (program.output.receive()) {
                    0L -> dir += Direction.WEST
                    1L -> dir += Direction.EAST
                }
                painted += pos
                pos += dir
            }
        }
        return painted.size to Grid<Char>().apply { set(seen, '█') }
    }
}