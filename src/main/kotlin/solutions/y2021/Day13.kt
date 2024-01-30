package solutions.y2021

import Solution
import utils.Utils.extractNumbers
import utils.Utils.sdanl
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day13 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val (grid, folds) = parse(input)
        return grid.fold(if ('x' in folds.first()) Point(0, folds.first().extractNumbers().toInt()) else Point(folds.first().extractNumbers().toInt(), 0)).size
    }

    override fun solvePart2(input: File): Any {
        var (grid, folds) = parse(input)
        folds.forEach {
            grid = grid.fold(if ('x' in it) Point(0, it.extractNumbers().toInt()) else Point(it.extractNumbers().toInt(), 0))
        }
        return grid.map { '#' }
    }

    private fun parse(input: File): Pair<Grid<Boolean>, List<String>> {
        val (points, folds) = input.sdanl()
        return Pair(Grid<Boolean>().apply { data = points.associate { Point.of(it).invert() to true }.toMutableMap() }, folds)
    }

    private fun Grid<Boolean>.fold(f: Point): Grid<Boolean> {
        val newGrid = Grid<Boolean>()
        this.forEachIndexed { p, v ->
            newGrid[Point(if (f.x != 0 && p.x > f.x) f.x - (p.x - f.x) else p.x, if (f.y != 0 && p.y > f.y) f.y - (p.y - f.y) else p.y)] = v
        }
        return newGrid
    }
}