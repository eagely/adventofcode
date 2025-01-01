package solutions.y2017

import Solution
import utils.chargrid
import utils.die
import utils.movement.Direction
import utils.point.Point
import java.io.File
import kotlin.collections.set

class Day22 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        var (next, grid) = with(input.chargrid()) {
            (Point(rows / 2, columns / 2) to Direction.NORTH) to filter { it == '#' }.data.keys.toMutableSet()
        }
        var c = 0
        repeat(10000) {
            val (p, d) = next
            val v = p in grid
            val nd = d + if (v) Direction.EAST else Direction.WEST
            if (v) grid.remove(p) else {
                grid.add(p)
                c++
            }
            next = p + nd to nd
        }
        return c

    }

    override fun solvePart2(input: File): Any {
        var (next, grid) = with(input.chargrid()) {
            (Point(rows / 2, columns / 2) to Direction.NORTH) to filter { it == '#' }.data.keys.associateWith { 2 }.toMutableMap().withDefault { 0 }
        }
        var c = 0
        repeat(10000000) {
            val (p, d) = next
            val v = grid.getValue(p)
            val nd = d + when (v) {
                0 -> Direction.WEST
                1 -> Direction.NORTH
                2 -> Direction.EAST
                3 -> Direction.SOUTH
                else -> die()
            }
            grid[p] = (v + 1) % 4
            if (v == 1) c++

            next = p + nd to nd
        }
        return c
    }
}