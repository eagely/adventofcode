package solutions.y2022

import Solution
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.Direction
import utils.movement.Direction.*
import utils.point.Point
import java.io.File
import java.util.*

class Day23 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        val dirs = ArrayDeque(listOf(NORTH, SOUTH, WEST, EAST))
        var elves = Grid.of(input.rl()).getPointsWithValue('#').toHashSet()
        repeat(10) {
            elves = playRound(elves, dirs)
            dirs.add(dirs.remove())
        }
        return ((elves.maxOf { it.x } - elves.minOf { it.x } + 1) * (elves.maxOf { it.y } - elves.minOf { it.y } + 1)) - elves.size
    }

    private fun playRound(elves: HashSet<Point>, dirs: ArrayDeque<Direction>): HashSet<Point> {
        val prop = HashMap<Point, Point>()
        val dupes = HashSet<Point>()
        for (elf in elves) {
            if (canMove(elves, elf, dirs.toList())) continue
            for (dir in dirs) {
                var next = elf.gridPlus(dir)
                if (canMove(elves, elf, dir)) {
                    if (prop.values.contains(next)) dupes.add(next)
                    prop[elf] = next
                    break
                }
            }
        }
        for ((elf, proposed) in prop) {
            if (dupes.contains(proposed)) continue
            elves.remove(elf)
            elves.add(proposed)
        }
        return elves
    }

    private fun canMove(elves: Set<Point>, elf: Point, dirs: List<Direction>) = dirs.all { canMove(elves, elf, it) }

    private fun canMove(elves: Set<Point>, elf: Point, dir: Direction) = dir.extendOnGrid().none { s -> elves.contains(elf + s) }

    override fun solvePart2(input: File): Any {
        val dirs = ArrayDeque(listOf(NORTH, SOUTH, WEST, EAST))
        var round = 1

        var elves = Grid.of(input.rl()).getPointsWithValue('#').toHashSet()
        var prev = elves.map { it }.toHashSet()
        while (true) {
            elves = playRound(elves, dirs)
            dirs.add(dirs.remove())
            if (elves == prev) break
            prev = elves.map { it }.toHashSet()
            round++
        }
        return round
    }
}
