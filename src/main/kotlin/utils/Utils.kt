package utils

import utils.grid.Grid
import utils.point.Point
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.security.MessageDigest
import java.util.*
import kotlin.math.pow
import kotlin.math.sign
import kotlin.random.Random

object Utils {

    fun String.extractNumbers(): String = this.filter { it.isDigit() }
    fun String.extractNegatives(): String {
        return Regex("-?\\d+").findAll(this).joinToString(separator = ",") { it.value }
    }

    fun String.extractLetters(): String = this.filter { it.isLetter() }
    fun String.removeTrailingNumbers(): String = this.replace(Regex("\\d+$"), "")
    fun String.containsNumber(): Boolean = this.contains(Regex("\\d+"))
    fun String.toChar(): Char = if (this.l != 1) throw IllegalArgumentException("String of length other than 1 cannot be converted to a Char") else this.toCharArray().first()

    fun Char.asInt() = this.toString().toInt()
    infix fun <T> List<T>.at(pos: Int) = this[pos % this.size]
    infix fun String.at(pos: Int) = this[pos % this.length]
    fun Double.format(scale: Int) = "%.${scale}f".format(this)
    fun Float.format(scale: Int) = "%.${scale}f".format(this)
    fun Int.abs() = Math.abs(this)
    fun Long.abs() = Math.abs(this)
    fun Long.pow(power: Int): Long = this.toDouble().pow(power).toLong()
    fun Int.pow(power: Int): Int = this.toDouble().pow(power).toInt()
    fun <T> List<T>.isAllEqual(): Boolean {
        for (i in 1..<this.size) if (this[i] != this[i - 1]) return false
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

    inline fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
        val list = ArrayList<T>()
        for (item in this) {
            list.add(item)
            if (predicate(item)) break
        }
        return list
    }

    fun Iterable<Int>.product(): Int = reduce { a, b -> a * b }

    fun List<IntRange>.reduce(): List<IntRange> = if (this.size <= 1) this
    else {
        val sorted = this.sortedBy { it.first }
        sorted.drop(1).fold(mutableListOf(sorted.first())) { reduced, range ->
            val lastRange = reduced.last()
            if (range.first <= lastRange.last) reduced[reduced.lastIndex] = (lastRange.first..maxOf(lastRange.last, range.last))
            else reduced.add(range)
            reduced
        }
    }

    fun <T> List<T>.nth(n: Int): T = this[n % size]

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
    infix fun Set<*>.and(other: Set<*>): Set<*> = this.intersect(other)
    infix fun Set<*>.or(other: Set<*>): Set<*> = this.union(other)
    infix fun Set<*>.xor(other: Set<*>): Set<*> = this.union(other).minus(this.intersect(other))
    infix fun String.hash(algorithm: String) = MessageDigest.getInstance(algorithm).digest(this.toByteArray()).joinToString("") { "%02x".format(it) }

    fun copyToClipboard(content: String) = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(content), null)

    fun <T> List<T>.zipWithAll(): List<Pair<T, T>> {
        val result = mutableListOf<Pair<T, T>>()
        for (i in this.indices) {
            for (j in this.indices) {
                if (i != j) {
                    result.add(Pair(this[i], this[j]))
                }
            }
        }
        return result
    }

    fun <T> List<T>.zipWithAllUnique(): List<Pair<T, T>> {
        val result = mutableListOf<Pair<T, T>>()
        val seenPairs = mutableSetOf<Pair<T, T>>()

        for (i in this.indices) {
            for (j in this.indices) {
                if (i != j) {
                    val pair = Pair(this[i], this[j])
                    val reversePair = Pair(this[j], this[i])
                    if (reversePair !in seenPairs) {
                        result.add(pair)
                        seenPairs.add(pair)
                    }
                }
            }
        }
        return result
    }

    infix fun <T> Int.from(list: List<T>) = list.take(this)
    infix fun <T> List<T>.fetch(amt: Int) = this.take(amt)
    infix fun <T> List<T>.after(index: Int) = this.subList(index + 1, this.size)
    infix fun <T> List<T>.after(elem: T) = this.subList(this.indexOf(elem) + 1, this.size)
    infix fun <T> List<T>.before(index: Int) = this.subList(0, index)
    infix fun <T> List<T>.before(elem: T) = this.subList(0, this.indexOf(elem))
    infix fun String.matching(other: String): String = this.zip(other).filter { (a, b) -> a == b }.map { it.first }.joinToString("")
    infix fun String.nonmatching(other: String): String = this.zip(other).filter { (a, b) -> a != b }.map { it.first }.joinToString("")
    fun String.halve() = this.splitAt(this.l / 2)
    fun String.split() = this.split(" ").dropBlanks()
    fun <T> Collection<T>.dropBlanks() = this.filter { it.toString().isNotBlank() }
    fun List<String>.split() = this.map { it.split() }
    operator fun IntRange.plus(other: Int) = (this.first + other)..(this.last + other)
    operator fun IntRange.minus(other: Int) = (this.first - other)..(this.last - other)
    operator fun LongRange.plus(other: Long) = (this.first + other)..(this.last + other)
    operator fun LongRange.minus(other: Long) = (this.first - other)..(this.last - other)
    infix fun String.splitAt(index: Int) = Pair(this.substring(0, index), this.substring(index))
    infix fun <T, R> Iterable<T>.mp(transform: (T) -> R) = this.map(transform)
    fun String.distinct() = this.toSet().joinToString("")
    fun String.duplicates(): String = this.groupingBy { it }.eachCount().filter { it.value > 1 }.flatMap { (char, count) -> List(count) { char } }.joinToString("")

    fun List<String>.containsLength(length: Int) = this.any { it.l == length }
    fun String.contains(char: Char, count: Int): Boolean = this.count { it == char } == count
    fun String.consecutive(): List<String> = this.fold(mutableListOf<String>()) { acc, char ->
        if (acc.isEmpty() || acc.last().last() != char) {
            acc.add(char.toString())
        } else {
            acc[acc.lastIndex] = acc.last() + char
        }
        acc
    }

    fun File.rl(): List<String> = this.readLines().dropLastWhile { it.isBlank() }
    fun File.rt(): String = this.readText().trim()
    fun File.sdnl() = this.rt().split("\n\n")
    fun List<String>.snl() = this.map { it.split("\n") }
    fun Collection<Point>.getNeighbors() = this.flatMap { it.getNeighbors() }.toSet()
    fun Collection<Point>.getCardinalNeighbors() = this.flatMap { it.getCardinalNeighbors() }.toSet()
    fun <T> Collection<T>.filterConsecutive(predicate: (T) -> Boolean): List<List<T>> {
        val result = mutableListOf<List<T>>()
        var currentList = mutableListOf<T>()

        for (element in this) {
            if (predicate(element)) {
                currentList.add(element)
            } else if (currentList.isNotEmpty()) {
                result.add(currentList)
                currentList = mutableListOf()
            }
        }

        if (currentList.isNotEmpty()) {
            result.add(currentList)
        }

        return result
    }

    fun <T> Collection<T>.undistinct(): List<T> {
        val frequencyMap = this.groupingBy { it }.eachCount()
        return this.filter { frequencyMap[it]!! > 1 }
    }
    operator fun <T> Collection<T>.get(index: Int): T = if (index.sign == -1) this[this.size-index] else this[index]
    operator fun <T> Collection<T>.get(range: IntRange): List<T> = this.toList().subList(range.first, range.last + 1)
    operator fun <T> MutableList<T>.set(range: IntRange, value: T) {
        range.forEach { this[it] = value }
    }

    val String.l: Int get() = this.length
    val Collection<*>.s: Int get() = this.size


    fun <T> bfsPath(
        start: T,
        isEnd: (T) -> Boolean,
        getNext: (T) -> Collection<T>,
        getStepCost: (T) -> Int = { 1 }
    ): Pair<List<T>, Int>? {
        if (isEnd(start)) return Pair(listOf(start), 0)

        val visited = mutableSetOf<T>()
        val queue: Deque<Pair<T, Pair<List<T>, Int>>> = LinkedList()

        queue.offer(Pair(start, Pair(listOf(start), 0)))
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, pathWithCost) = queue.poll()
            val (path, cost) = pathWithCost

            if (isEnd(current)) return Pair(path, cost)

            for (next in getNext(current)) {
                if (next !in visited) {
                    visited.add(next)
                    queue.offer(Pair(next, Pair(path + next, cost + getStepCost(next))))
                }
            }
        }

        return null
    }


    fun <T> bfsCost(
        start: T,
        isEnd: (T) -> Boolean,
        getNext: (T) -> Collection<T>,
        getStepCost: (T) -> Int = { 1 }
    ): Int? {
        if (isEnd(start)) return 0

        val visited = mutableSetOf<T>()
        val queue: Deque<Pair<T, Int>> = LinkedList()

        queue.offer(Pair(start, 0))
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, cost) = queue.poll()

            if (isEnd(current)) {
                return cost
            }

            for (next in getNext(current)) {
                if (next !in visited) {
                    visited.add(next)
                    queue.offer(Pair(next, cost + getStepCost(next)))
                }
            }
        }

        return null
    }
}