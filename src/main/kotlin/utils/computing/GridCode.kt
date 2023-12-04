package utils.computing

import utils.Utils.product
import utils.grid.Grid
import utils.grid.Grid.Companion.numberAt
import utils.point.Point

data class GridCode(private val program: Grid<Char>) {
    var pointer = Point(0, 0)
    private val code = program.filter { it.toString().matches("[^0-9.]".toRegex()) }
    fun run(): List<Int> {
        val output = mutableListOf<Int>()
        code.forEachIndexed {
            index, value ->
            pointer = index
            when (value) {
                '*' -> {
                    output += index
                        .getNeighbors()
                        .mapNotNull { program.numberAt(it)?.toIntOrNull() }
                        .distinct()
                        .let { if (it.size == 2) it.product() else 0 }
                }
                '+' -> {
                    output += index
                        .getNeighbors()
                        .mapNotNull { program.numberAt(it)?.toIntOrNull() }
                        .distinct()
                        .let { if (it.size == 2) it.sum() else 0 }
                }
                '-' -> {
                    output += index
                        .getNeighbors()
                        .mapNotNull { program.numberAt(it)?.toIntOrNull() }
                        .distinct()
                        .let { if (it.size == 2) it.reduce { acc, i -> acc - i } else 0 }
                }
                '/' -> {
                    output += index
                        .getNeighbors()
                        .mapNotNull { program.numberAt(it)?.toIntOrNull() }
                        .distinct()
                        .let { if (it.size == 2) it.reduce { acc, i -> acc / i } else 0 }
                }
                '%' -> {
                    output += index
                        .getNeighbors()
                        .mapNotNull { program.numberAt(it)?.toIntOrNull() }
                        .distinct()
                        .let { if (it.size == 2) it.reduce { acc, i -> acc % i } else 0 }
                }
                '#' -> {
                }
                '&' -> {
                }
                '@' -> {
                }
                '$' -> {
                }
                '=' -> {
                }
            }
        }
        return output
    }
}