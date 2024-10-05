package solutions.y2018

import Solution
import utils.*
import utils.annotations.NoTest
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day13 : Solution(2018) {

    private val cartTypes = "^>v<"
    private val turns = listOf(Direction.WEST, Direction.NORTH, Direction.EAST)

    private data class Cart(var pos: Point, var dir: Direction, var turn: Int)

    override fun solvePart1(input: File): Any {
        val (grid, carts) = parse(input)
        while (true) {
            for (c in carts.sortedWith(compareBy<Cart> { it.pos.x }.thenBy { it.pos.y })) {
                c.pos += c.dir
                c.rotate(grid[c.pos]!!)
                carts.firstDuplicateOfOrNull { it.pos }?.let { return "${it.y},${it.x}" }
            }
        }
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        val (grid, carts) = parse(input)
        while (carts.size != 1) {
            for (c in carts.sortedWith(compareBy<Cart> { it.pos.x }.thenBy { it.pos.y }).toSet()) {
                c.pos += c.dir
                c.rotate(grid[c.pos]!!)
                carts.removeAll(carts.filter { a -> carts.count { it.pos == a.pos } > 1 })
            }
        }
        return carts.first().pos.let { "${it.y},${it.x}" }
    }

    private fun parse(input: File): Pair<Grid<Char>, MutableList<Cart>> {
        val grid = input.chargrid()
        return grid.map {
            when (it) {
                '^', 'v' -> '|'
                '>', '<' -> '-'
                else -> it
            }
        } to grid.data.filterValues { it in cartTypes }.map { (k, v) -> Cart(k, Direction.of(cartTypes.indexOf(v) * 90), 0) }.toMutableList()
    }

    private fun Cart.rotate(next: Char) {
        if (next !in "-|") {
            this.dir = when (next) {
                '/' -> when (this.dir) {
                    Direction.NORTH, Direction.SOUTH -> this.dir.right
                    else -> this.dir.left
                }

                '\\' -> when (this.dir) {
                    Direction.NORTH, Direction.SOUTH -> this.dir.left
                    else -> this.dir.right
                }

                '+' -> this.dir + turns[this.turn++ % 3]
                else -> die()
            }
        }
    }
}
