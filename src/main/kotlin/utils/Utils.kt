package utils

import utils.grid.Grid
import utils.point.Point
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign
import kotlin.random.Random

@Suppress("unused", "MemberVisibilityCanBePrivate")
object Utils {

    fun String.extractNumbers(): String = this.filter { it.isDigit() }
    fun String.extractNegatives(): String {
        return Regex("-?\\d+").findAll(this).joinToString(separator = ",") { it.value }
    }

    fun String.extractLetters(): String = this.filter { it.isLetter() }
    fun String.extractSpecial(): String = this.filter { !it.isLetter() && !it.isDigit() }
    fun String.extractNumbersSeparated(): List<Int> = this.split(Regex("\\D+")).filter { it.isNotBlank() }.map { it.toInt() }
    fun String.extractNegativesSeparated(): List<Int> = this.split(Regex("[^-\\d]+")).filter { it.isNotBlank() }.map { it.toInt() }
    fun String.removeTrailingNumbers(): String = this.replace(Regex("\\d+$"), "")
    fun String.containsNumber(): Boolean = this.contains(Regex("\\d+"))
    fun String.toChar(): Char = if (this.l != 1) throw IllegalArgumentException("String of length other than 1 cannot be converted to a Char") else this.toCharArray().first()

    fun Char.asInt() = this.toString().toInt()
    infix fun <T> List<T>.at(pos: Int) = this[pos % this.size]
    infix fun String.at(pos: Int) = this[pos % this.length]
    fun Double.format(scale: Int) = "%.${scale}f".format(this)
    fun Float.format(scale: Int) = "%.${scale}f".format(this)
    fun Int.abs() = abs(this)
    fun Long.abs() = abs(this)
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
    fun <T> List<T>.after(index: Int) = this.subList(index + 1, this.size)
    fun <T> List<T>.after(elem: T) = this.subList(this.indexOf(elem) + 1, this.size)
    fun <T> List<T>.before(index: Int) = this.subList(0, index)
    fun <T> List<T>.before(elem: T) = this.subList(0, this.indexOf(elem))

    fun String.after(char: Char) = this.substringAfter(char)
    fun String.before(char: Char) = this.substringBefore(char)
    fun String.after(str: String) = this.substringAfter(str)
    fun String.before(str: String) = this.substringBefore(str)
    fun String.after(regex: Regex) = this.substringAfter(regex.find(this)?.value ?: "")
    fun String.before(regex: Regex) = this.substringBefore(regex.find(this)?.value ?: "")
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
    fun String.consecutive(): List<String> = this.fold(mutableListOf()) { acc, char ->
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

    operator fun <T> Collection<T>.get(index: Int): T = if (index.sign == -1) this[this.size - index] else this[index]
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
        getStepCost: (T) -> Int = { 1 },
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
        getStepCost: (T) -> Int = { 1 },
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

    fun String.scanf(format: String, stripEnd: String = ""): List<String> {
        var str = this
        var newf = format.ifNotEndsWith("EOF") { it + "EOF" }
        val results = mutableListOf<String>()
        val pattern = """%s([^%]*)\.\.\.""".toRegex()
        val delimiters = pattern.findAll(format).map { it.value.after("%s").before("...") }.toMutableList()
        while (newf.contains(pattern)) {
            if (str.indexOf(delimiters.first()) > str.indexOf(delimiters[1 % delimiters.size])) {
                delimiters.removeFirst()
            }
            val before = newf.before("%s")
            str = str.after(before).substringBeforeLast(format.substringAfterLast("..."))
            if (delimiters.size > 1) {
                results.addAll(str.before(delimiters[1]).split(delimiters.first()))
            } else {
                results.addAll(str.split(delimiters.first()))
                break
            }
            str = str.after(delimiters[1])
            if (str == "EOF") {
                break
            }
        }
        newf = format
        str = this
        while (newf.contains("%s")) {
            val before = newf.before("%s")
            val after = newf.after("%s").first()
            if (newf.after("%s") == "EOF") {
                results.add(str.after(before))
                break
            }
            results.add(str.after(before).before(after))
            str = str.after(str[str.indexOf(after) - 1])
            newf = newf.after("%s")
        }

        results.add(results.removeLast().replace(stripEnd, ""))

        return results.dropBlanks()
    }

    fun <T> Collection<T>.destructure() = this.first() to this.drop(1)
    fun gcd(a: Long, b: Long): Long {
        if (b == 0L) return a
        return gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return abs(a * b) / gcd(a, b)
    }

    fun lcm(a: BigInteger, b: BigInteger): BigInteger {
        return a.multiply(b).divide(a.gcd(b))
    }

    fun lcm(vararg nums: BigInteger): BigInteger {
        return nums.reduce { a, b -> lcm(a, b) }
    }

    fun lcm(nums: Set<BigInteger>): BigInteger {
        return nums.reduce { a, b -> lcm(a, b) }
    }

    fun String.uniques(): Int = distinct().count()
    fun String.counts(): Map<Char, Int> = groupingBy { it }.eachCount()
    fun String.ifNotContains(char: Char, action: (String) -> (String)): String = if (this.contains(char)) this else action(this)
    fun String.ifContains(char: Char, action: (String) -> (String)): String = if (this.contains(char)) action(this) else this
    fun String.ifNotStartsWith(char: Char, action: (String) -> (String)): String = if (this.startsWith(char)) this else action(this)
    fun String.ifStartsWith(char: Char, action: (String) -> (String)): String = if (this.startsWith(char)) action(this) else this
    fun String.ifNotEndsWith(char: Char, action: (String) -> (String)): String = if (this.endsWith(char)) this else action(this)
    fun String.ifEndsWith(char: Char, action: (String) -> (String)): String = if (this.endsWith(char)) action(this) else this

    fun String.ifNotContains(str: String, action: (String) -> (String)): String = if (this.contains(str)) this else action(this)
    fun String.ifContains(str: String, action: (String) -> (String)): String = if (this.contains(str)) action(this) else this
    fun String.ifNotStartsWith(str: String, action: (String) -> (String)): String = if (this.startsWith(str)) this else action(this)
    fun String.ifStartsWith(str: String, action: (String) -> (String)): String = if (this.startsWith(str)) action(this) else this
    fun String.ifNotEndsWith(str: String, action: (String) -> (String)): String = if (this.endsWith(str)) this else action(this)
    fun String.ifEndsWith(str: String, action: (String) -> (String)): String = if (this.endsWith(str)) action(this) else this
    fun String.ifEquals(str: String, action: (String) -> (String)): String = if (this == str) action(this) else this
    fun String.ifNotEquals(str: String, action: (String) -> (String)): String = if (this != str) this else action(this)
    fun String.ifEquals(char: Char, action: (String) -> (String)): String = if (this == char.toString()) action(this) else this
    fun String.ifNotEquals(char: Char, action: (String) -> (String)): String = if (this != char.toString()) this else action(this)
    fun String.ifNotContains(regex: Regex, action: (String) -> (String)): String = if (this.contains(regex)) this else action(this)
    fun String.ifContains(regex: Regex, action: (String) -> (String)): String = if (this.contains(regex)) action(this) else this
    fun String.ifNotStartsWith(regex: Regex, action: (String) -> (String)): String = if (this.startsWith(regex)) this else action(this)
    fun String.ifStartsWith(regex: Regex, action: (String) -> (String)): String = if (this.startsWith(regex)) action(this) else this
    fun String.ifNotEndsWith(regex: Regex, action: (String) -> (String)): String = if (this.endsWith(regex)) this else action(this)
    fun String.ifEndsWith(regex: Regex, action: (String) -> (String)): String = if (this.endsWith(regex)) action(this) else this
    fun String.ifNotMatches(regex: Regex, action: (String) -> (String)): String = if (this.matches(regex)) this else action(this)
    fun String.ifMatches(regex: Regex, action: (String) -> (String)): String = if (this.matches(regex)) action(this) else this
    fun String.endsWith(regex: Regex): Boolean {
        return this.endsWith(regex.find(this)?.value ?: return false)
    }

    fun String.startsWith(regex: Regex): Boolean {
        return this.startsWith(regex.find(this)?.value ?: return false)
    }

    fun String.trimMultiSpace(): String {
        return this.replace(Regex("\\s+"), " ")
    }

    fun chineseRemainder(mod: List<Long>, rem: List<Long>): Long {
        val prod = mod.fold(1L) { acc, i -> acc * i }

        return mod.zip(rem).sumOf { (moduli, remainder) ->
            val p = prod / moduli
            remainder * modularMultiplicativeInverse(p, moduli) * p
        } % prod
    }

    fun modularMultiplicativeInverse(a: Long, m: Long): Long {
        var y = 0L
        var x = 1L

        if (m == 1L) return 0L

        var aTemp = a
        var mTemp = m

        while (aTemp > 1) {
            val q = aTemp / mTemp
            var t = mTemp

            mTemp = aTemp % mTemp
            aTemp = t
            t = y

            y = x - q * y
            x = t
        }

        return if (x < 0) x + m else x
    }

    fun multiplyMatrices(first: Array<IntArray>, second: Array<IntArray>): Array<IntArray>? {
        if (first.first().size != second.size) return null

        return Array(first.size) { row ->
            IntArray(second.first().size) { col ->
                first[row].indices.sumOf { k -> first[row][k] * second[k][col] }
            }
        }
    }

    fun addMatrices(first: Array<IntArray>, second: Array<IntArray>): Array<IntArray>? {
        if (first.size != second.size || first.first().size != second.first().size) {
            return null
        }

        return Array(first.size) { i ->
            IntArray(first[i].size) { j ->
                first[i][j] + second[i][j]
            }
        }
    }

    fun subtractMatrices(first: Array<IntArray>, second: Array<IntArray>): Array<IntArray>? {
        if (first.size != second.size || first.first().size != second.first().size) {
            return null
        }

        return Array(first.size) { i ->
            IntArray(first[i].size) { j ->
                first[i][j] - second[i][j]
            }
        }
    }

    fun getPolygonArea(vertices: List<Point>): Double {
        if (vertices.size < 3) return 0.0

        var sum1 = 0.0
        var sum2 = 0.0

        for (i in vertices.indices) {
            val current = vertices[i]
            val next = vertices[(i + 1) % vertices.size]

            sum1 += current.x * next.y
            sum2 += current.y * next.x
        }

        return 0.5 * abs(sum1 - sum2)
    }
}