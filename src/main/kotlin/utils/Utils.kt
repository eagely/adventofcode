package utils

import utils.grid.Grid
import utils.point.Point
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.random.Random

object Utils {
    fun <T> gridOf(rows: List<List<T?>>): Grid<T?> {
        val grid = Grid<T?>()
        for (i in rows.indices) {
            for (j in rows[i].indices) {
                grid.set(i, j, rows[i][j])
            }
        }
        return grid
    }

    fun String.extractNumbers(): String = this.filter { it.isDigit() }
    fun String.extractLetters(): String = this.filter { it.isLetter() }
    fun String.removeTrailingNumbers(): String = this.replace(Regex("\\d+$"), "")
    fun Double.format(scale: Int) = "%.${scale}f".format(this)
    fun Float.format(scale: Int) = "%.${scale}f".format(this)
    fun <T> List<T>.isAllEqual(): Boolean {
        for (i in 1 ..< this.size) if (this[i] != this[i - 1]) return false
        return true
    }

    fun <T> List<T>.allEquals(value: T): Boolean {
        for (i in this) if (i != value) return false
        return true
    }

    fun <T> List<T>.allContains(value: T): Boolean {
        for (i in this) if (!i.toString().contains(value.toString())) return false
        return true
    }

    data class Node(val point: Point, val parent: Node? = null)

    fun heuristic(node: Node, goal: Node): BigDecimal {
        return (abs(node.point.x.toDouble() - goal.point.x.toDouble()) + abs(node.point.y.toDouble() - goal.point.y.toDouble())).toBigDecimal()
    }

    fun aStar(grid: Grid<Boolean>, start: Point, end: Point): List<Point> {
        val openSet = mutableListOf<Node>()
        val closedSet = mutableListOf<Node>()
        val startNode = Node(start)
        val goalNode = Node(end)
        openSet.add(startNode)

        while (openSet.isNotEmpty()) {
            val currentNode = openSet.minByOrNull { heuristic(it, goalNode) }!!
            if (currentNode.point == goalNode.point) {
                var node = currentNode
                val path = mutableListOf<Point>()
                while (node.parent != null) {
                    path.add(node.point)
                    node = node.parent!!
                }
                return path.reversed()
            }

            openSet.remove(currentNode)
            closedSet.add(currentNode)

            for (neighbor in grid.getNeighborPositions(currentNode.point.x.toInt(), currentNode.point.y.toInt())) {
                if (grid.getValue(neighbor.x.toInt(), neighbor.y.toInt()) == true || closedSet.any { it.point == neighbor }) {
                    continue
                }

                val neighborNode = Node(neighbor, currentNode)
                if (!openSet.any { it.point == neighbor } || heuristic(neighborNode, goalNode) < heuristic(currentNode, goalNode)) {
                    openSet.add(neighborNode)
                }
            }
        }

        return emptyList()
    }

    fun generateRandomGrid(rows: Int, columns: Int, obstacleProbability: Double): Grid<Boolean> {
        val grid = Grid<Boolean>(rows, columns)
        for (row in 0..<rows) {
            for (column in 0..<columns) {
                grid.set(row, column, Random.nextDouble() < obstacleProbability)
            }
        }
        return grid
    }

    fun toFahrenheit(celsius: Double) = celsius * 9 / 5 + 32
    fun toCelsius(fahrenheit: Double) = (fahrenheit - 32) * 5 / 9
    fun Any?.print() = print(this)
    fun Any?.println() = println(this)
}