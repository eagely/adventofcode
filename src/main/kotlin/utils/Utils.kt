package utils

import utils.grid.Grid
import utils.point.Point
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

object Utils {

    fun String.extractNumbers(): String = this.filter { it.isDigit() }
    fun String.extractNegatives(): String {
        return Regex("-?\\d+").findAll(this)
            .joinToString(separator = ",") { it.value }
    }
    fun String.extractLetters(): String = this.filter { it.isLetter() }
    fun String.removeTrailingNumbers(): String = this.replace(Regex("\\d+$"), "")
    fun String.containsNumber(): Boolean = this.contains(Regex("\\d+"))
    fun String.toChar(): Char = if(this.l != 1) throw IllegalArgumentException("String of length other than 1 cannot be converted to a Char") else this.toCharArray().first()
    fun Double.format(scale: Int) = "%.${scale}f".format(this)
    fun Float.format(scale: Int) = "%.${scale}f".format(this)
    fun Int.abs() = Math.abs(this)
    fun Long.abs() = Math.abs(this)
    fun Int.pow(power: Int): Int = this.pow(power)
    fun Long.pow(power: Int): Long = this.toDouble().pow(power).toLong()
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

    fun generateRandomGrid(rows: Int, columns: Int, obstacleProbability: Double): Grid<Boolean> {
        val grid = Grid<Boolean>(rows, columns)
        for (row in 0..<rows) {
            for (column in 0..<columns) {
                grid[row, column] = Random.nextDouble() < obstacleProbability
            }
        }
        return grid
    }

    fun toFahrenheit(celsius: Double) = celsius * 9 / 5 + 32
    fun toCelsius(fahrenheit: Double) = (fahrenheit - 32) * 5 / 9

    inline fun <G> Iterable<G>.takeUntil(predicate: (G) -> Boolean): List<G> {
        val list = ArrayList<G>()
        for (item in this) {
            list.add(item)
            if (predicate(item))
                break
        }
        return list
    }
    fun Iterable<Int>.product(): Int =
        reduce { a, b -> a * b }

    fun List<IntRange>.reduce(): List<IntRange> =
        if (this.size <= 1) this
        else {
            val sorted = this.sortedBy { it.first }
            sorted.drop(1).fold(mutableListOf(sorted.first())) { reduced, range ->
                val lastRange = reduced.last()
                if (range.first <= lastRange.last)
                    reduced[reduced.lastIndex] = (lastRange.first..maxOf(lastRange.last, range.last))
                else
                    reduced.add(range)
                reduced
            }
        }

    fun <T> List<T>.nth(n: Int): T =
        this[n % size]
    fun Any?.print(): Any? {
        print(this)
        return this
    }
    fun Any?.println(): Any? {
        println(this)
        return this
    }
    infix fun Long.posmod(mod: Int): Int {
        return (this.mod(mod) + mod) % mod
    }
    infix fun Int.posmod(mod: Int): Int {
        return (this % mod + mod) % mod
    }
    infix fun Int.p(y: Int): Point = Point(this, y)
    infix fun Set<*>.and (other: Set<*>): Set<*> = this.intersect(other)
    infix fun Set<*>.or (other: Set<*>): Set<*> = this.union(other)
    infix fun Set<*>.xor (other: Set<*>): Set<*> = this.union(other).minus(this.intersect(other))
    infix fun Set<*>.without (other: Set<*>): Set<*> = this.minus(other)
    fun copyToClipboard(content: String) = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(content), null)
    fun File.rl(): List<String> = this.readLines().dropLastWhile { it.isBlank() }
    fun File.rt(): String = this.readText().trim()
    val String.l: Int get() = this.length
    val Collection<*>.s: Int get() = this.size
}