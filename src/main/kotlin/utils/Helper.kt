@file:Suppress("unused")

package utils

import net.objecthunter.exp4j.ExpressionBuilder
import utils.grid.Grid
import utils.point.LongPoint
import utils.point.Point
import utils.point.Point3D
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.full.primaryConstructor

/**
 * Privates
 */
private val md5 = MessageDigest.getInstance("MD5")

/**
 * Parsing
 */
val File.lines get() = rl()
val File.text get() = rt()
val File.ints get() = ril()
val File.longs get() = rll()
fun String.extractNumbers() = this.filter { it.isDigit() }
fun String.extractNegatives() = Regex("-?\\d+").findAll(this).joinToString(separator = ",") { it.value }
fun String.extractLetters() = this.filter { it.isLetter() }
fun String.extractSpecial() = this.filter { !it.isLetter() && !it.isDigit() }
fun String.extractNumbersSeparated() = this.split(Regex("\\D+")).filter { it.isNotBlank() }.map { it.toInt() }
fun String.extractNegativesSeparated() = this.split(Regex("[^-\\d]+")).filter { it.isNotBlank() }.map { it.toInt() }
fun String.extractLongsSeparated() = this.split(Regex("\\D+")).filter { it.isNotBlank() }.map { it.toLong() }
fun String.extractNegativeLongsSeparated() = this.split(Regex("[^-\\d]+")).filter { it.isNotBlank() }.map { it.toLong() }
val String.ints get() = this.extractNegativesSeparated()


/**
 * Shorthand helpers
 */
// don't need switch cases for math
fun evalMathExpression(expression: String): Double {
    return ExpressionBuilder(expression).build().evaluate()
}

// evil reflection magic to cast a list to any dataclass
inline fun <reified T : Any> List<Any>.toDataClass(): T {
    val constructor = T::class.primaryConstructor!!
    return constructor.call(*this.toTypedArray())
}
fun String.removeTrailingNumbers() = this.replace(Regex("\\d+$"), "")
fun String.md5() = BigInteger(1, md5.digest(toByteArray())).toString(16).padStart(32, '0')
fun String.containsNumber() = this.contains(Regex("\\d+"))
fun String.hexToBin() = this.chunked(1).map { it.toInt(16).toString(2).padStart(4, '0') }.join()
fun String.binToHex() = this.chunked(4).map { it.toInt(2).toString(16) }.join()
fun String.replaceAt(range: IntRange, replacement: Char) = this.substring(0, range.first) + replacement + this.substring(range.last + 1)
fun String.replaceAt(range: IntRange, replacement: String) = this.substring(0, range.first) + replacement + this.substring(range.last + 1)
fun String.replaceAt(index: Int, replacement: Char) = this.substring(0, index) + replacement + this.substring(index + 1)
fun String.replaceAt(index: Int, replacement: String) = this.substring(0, index) + replacement + this.substring(index + 1)
fun String.insertAt(index: Int, char: Char) = this.substring(0, index) + char + this.substring(index)
fun String.insertAt(index: Int, string: String) = this.substring(0, index) + string + this.substring(index)
fun String.toChar() = if (this.l != 1) throw IllegalArgumentException("String of length other than 1 cannot be converted to a Char") else this.toCharArray().first()
fun rotateChar(c: Char, n: Int, start: Char, end: Char) = (start.code + ((c.code - start.code + n) % (end.code - start.code + 1))).toChar()
fun String.rotN(n: Int) = this.map { char ->
    when (char) {
        in 'A'..'Z' -> rotateChar(char, n, 'A', 'Z')
        in 'a'..'z' -> rotateChar(char, n, 'a', 'z')
        else -> char
    }
}.join()

fun <T> arrayDequeOf(c: T? = null) = if (c == null) ArrayDeque(listOf()) else ArrayDeque<T>(listOf(c))
fun <T> ArrayDeque<T>.rotate(n: Int) {
    if (n == 0) return
    if (n > 0) {
        repeat(n) {
            addFirst(removeLast())
        }
    } else {
        repeat(abs(n)) {
            addLast(removeFirst())
        }
    }
}

fun <T> Array<T>.join() = this.joinToString("")
fun <T> Array<T>.join(separator: String) = this.joinToString(separator)
fun CharArray.join() = this.joinToString("")
fun CharArray.join(separator: String) = this.joinToString(separator)
fun <T> Iterable<T>.join() = this.joinToString("")
fun <T> Iterable<T>.join(separator: String) = this.joinToString(separator)
fun CharIterator.next(n: Int): String {
    var res = ""
    repeat(n) {
        res += this.next()
    }
    return res
}

fun <T> Iterator<T>.next(n: Int): List<T> {
    val res = mutableListOf<T>()
    repeat(n) {
        res.add(this.next())
    }
    return res
}

fun Iterator<*>.skip(n: Int) {
    repeat(n) {
        if (!this.hasNext()) return
        this.next()
    }
}

fun List<Boolean>.toBitSet() = this.fold(BitSet()) { acc, b -> acc.apply { set(acc.size(), b) } }


fun String.isNumber() = this.all { it.isDigit() }
infix fun <T> List<T>.at(pos: Int) = this[pos % this.size]
infix fun String.at(pos: Int) = this[pos % this.length]
operator fun String.get(range: IntRange) = this.substring(range)
fun Double.format(scale: Int) = "%.${scale}f".format(this)
fun Float.format(scale: Int) = "%.${scale}f".format(this)
fun Int.abs() = abs(this)
fun Long.abs() = abs(this)
infix fun Long.pow(power: Int): Long = this.toDouble().pow(power).toLong()
infix fun Int.pow(power: Int): Int = this.toDouble().pow(power).toInt()
fun <T> List<T>.isAllEqual(): Boolean {
    for (i in 1..<this.size) if (this[i] != this[i - 1]) return false
    return true
}

fun <T> Collection<T>.allEquals(value: T): Boolean {
    for (i in this) if (i != value) return false
    return true
}

fun <T> Collection<T>.allContains(value: T): Boolean {
    for (i in this) if (!i.toString().contains(value.toString())) return false
    return true
}

fun <T> Collection<T>.firstDuplicateOrNull(): T? {
    val set = HashSet<T>()
    this.forEach {
        if (!set.add(it)) return it
    }
    return null
}

fun <T, R> Collection<T>.firstDuplicateOfOrNull(transform: (T) -> R): R? {
    val set = HashSet<R>()
    this.forEach {
        if (!set.add(transform(it))) return transform(it)
    }
    return null
}

fun <T, R> Collection<T>.firstDuplicateByOrNull(transform: (T) -> R): T? {
    val set = HashSet<R>()
    this.forEach {
        if (!set.add(transform(it))) return it
    }
    return null
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

fun <T> List<T>.separate(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<MutableList<T>>()
    var currentSublist = mutableListOf<T>()

    for (element in this) {
        if (predicate(element)) {
            result.add(currentSublist)
            currentSublist = mutableListOf()
        }
        currentSublist.add(element)
    }

    if (currentSublist.isNotEmpty()) {
        result.add(currentSublist)
    }

    return result.dropWhile { it.isEmpty() }.dropLastWhile { it.isEmpty() }
}


/**
 * Math
 */
fun ilog(x: Number, b: Number) = (log10(x.toDouble()) / log10(b.toDouble())).toInt()
fun Long.modPow(exponent: Long, mod: Long): Long {
    var result = 1L
    var b = this % mod
    var e = exponent
    while (e > 0) {
        if (e % 2 == 1L) result = (result * b) % mod
        b = (b * b) % mod
        e /= 2
    }
    return result
}

/**
 * Complex algos
 */
fun minCoins(coinValues: List<Long>, amount: Long): List<Long> {
    if (amount == 0L) return List(coinValues.size) { 0L }

    val coins = coinValues.sorted()
    val n = coins.size
    // Modulo range should cover at least a few multiples of the largest coin to establish a pattern
    val modValue = coins.last() * 10
    val dp = mutableMapOf<Long, List<Long>>()
    dp[0L] = List(n) { 0L } // Base case

    // Using dynamic programming to find minimum coins for every amount up to modValue
    for (i in 1..modValue) {
        var bestCombo: List<Long>? = null
        for ((index, coin) in coins.withIndex()) {
            if (i >= coin) {
                val previous = dp[i - coin]
                if (previous != null) {
                    val combo = previous.toMutableList()
                    combo[index]++
                    if (bestCombo == null || combo.sum() < bestCombo.sum()) {
                        bestCombo = combo
                    }
                }
            }
        }
        if (bestCombo != null) {
            dp[i] = bestCombo
        }
    }

    // Handling amounts larger than modValue by using the pattern found in dp
    val result = List(n) { 0L }.toMutableList()
    val fullCycles = amount / modValue
    val remainder = amount % modValue

    dp[remainder]?.let {
        for (i in result.indices) {
            result[i] = it[i] + fullCycles * (dp[modValue]?.get(i) ?: 0L)
        }
    }

    return result
}


@JvmName("mergeIntRanges")
fun merge(ranges: List<IntRange>): List<IntRange> {
    if (ranges.isEmpty()) return emptyList()
    val sortedRanges = ranges.sortedBy { it.first }
    val merged = mutableListOf<IntRange>()
    var currentMerge = sortedRanges[0]
    for (range in sortedRanges.drop(1)) {
        if (range.first <= currentMerge.last + 1) {
            currentMerge = currentMerge.first..maxOf(currentMerge.last, range.last)
        } else {
            merged.add(currentMerge)
            currentMerge = range
        }
    }
    merged.add(currentMerge)
    return merged
}

@JvmName("mergeLongRanges")
fun merge(ranges: List<LongRange>): List<LongRange> {
    if (ranges.isEmpty()) return emptyList()
    val sortedRanges = ranges.sortedBy { it.first }
    val merged = mutableListOf<LongRange>()
    var currentMerge = sortedRanges[0]
    for (range in sortedRanges.drop(1)) {
        if (range.first <= currentMerge.last + 1) {
            currentMerge = currentMerge.first..maxOf(currentMerge.last, range.last)
        } else {
            merged.add(currentMerge)
            currentMerge = range
        }
    }
    merged.add(currentMerge)
    return merged
}


fun Iterable<Int>.product(): Int = reduce { a, b -> a * b }
fun Iterable<Long>.product(): Long = reduce { a, b -> a * b }
fun Iterable<Double>.product(): Double = reduce { a, b -> a * b }
fun Iterable<Float>.product(): Float = reduce { a, b -> a * b }
fun Iterable<BigInteger>.product(): BigInteger = reduce { a, b -> a * b }
fun Iterable<BigDecimal>.product(): BigDecimal = reduce { a, b -> a * b }

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(predicate: (T) -> Int): Int = map(predicate).product()

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(predicate: (T) -> Long): Long = map(predicate).product()

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(predicate: (T) -> Double): Double = map(predicate).product()

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(predicate: (T) -> Float): Float = map(predicate).product()

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(predicate: (T) -> BigInteger): BigInteger = map(predicate).product()

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(predicate: (T) -> BigDecimal): BigDecimal = map(predicate).product()

val cardinalDirections get() = listOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))
val directions get() = cardinalDirections + listOf(Point(1, 1), Point(1, -1), Point(-1, 1), Point(-1, -1))
val IntRange.size get() = this.last - this.first + 1
val LongRange.size get() = this.last - this.first + 1
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

fun String.rotateRight(n: Int): String {
    val len = this.length
    if (len == 0) return this

    val effectiveShift = n % len
    return this.substring(len - effectiveShift) + this.substring(0, len - effectiveShift)
}

fun String.rotateLeft(n: Int): String {
    val len = this.length
    if (len == 0) return this

    val effectiveShift = n % len
    return this.substring(effectiveShift) + this.substring(0, effectiveShift)
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

infix fun Long.pm(other: Long): Long {
    val mod = this % other
    return if (mod < 0) mod + other else mod
}

infix fun Long.pm(other: Int): Int {
    val mod = this % other
    return if (mod < 0) mod.toInt() + other else mod.toInt()
}

infix fun Int.pm(other: Int): Int {
    val mod = this % other
    return if (mod < 0) mod + other else mod
}

infix fun Int.p(y: Int): Point = Point(this, y)
infix fun Point.p(z: Int): Point3D = Point3D(this.x, this.y, z)
infix fun Set<*>.and(other: Set<*>): Set<*> = this.intersect(other)
infix fun Set<*>.or(other: Set<*>): Set<*> = this.union(other)
infix fun Set<*>.xor(other: Set<*>): Set<*> = this.union(other).minus(this.intersect(other))
infix fun String.hash(algorithm: String) = MessageDigest.getInstance(algorithm).digest(this.toByteArray()).joinToString("") { "%02x".format(it) }

fun <T> Iterable<T>.cyclicIterator(): Iterator<T> {
    val list = this.toList()
    if (list.isEmpty()) throw NoSuchElementException("The collection is empty")
    return iterator {
        var index = 0
        while (true) {
            yield(list[index % list.size])
            index++
        }
    }
}

fun copyToClipboard(content: String) = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(content), null)

fun <T> List<T>.zipWithAll(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    for (i in this.indices) {
        for (j in i + 1 until this.size) {
            result.add(Pair(this[i], this[j]))
        }
    }
    return result
}

fun <T> List<T>.zipWithAllIfItsNonAssociative(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    for (i in this.indices) {
        for (j in i + 1 until this.size) {
            result.add(Pair(this[i], this[j]))
        }
    }
    for (i in this.indices.reversed()) {
        for (j in i - 1 downTo 0) {
            result.add(Pair(this[i], this[j]))
        }
    }
    return result.distinct()
}

fun <T> List<T>.zipWithAllUnique(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    val seen = mutableSetOf<T>()

    for (i in this.indices) {
        val element = this[i]
        result.addAll(seen.map { Pair(it, element) })
        seen.add(element)
    }

    return result
}

fun <T> List<T>.zipWithAll(zipSize: Int = 2): List<List<T>> {
    if (zipSize < 2) throw IllegalArgumentException("zipSize must be at least 2")

    fun combine(start: Int, current: MutableList<T>): List<List<T>> {
        if (current.size == zipSize) {
            return listOf(current.toList())
        }
        val combinations = mutableListOf<List<T>>()
        for (i in start until this.size) {
            current.add(this[i])
            combinations += combine(i + 1, current)
            current.removeAt(current.lastIndex) // Backtrack
        }
        return combinations
    }

    return combine(0, mutableListOf())
}

fun <T> List<T>.zipWithAllUnique(zipSize: Int = 2): List<List<T>> {
    if (zipSize < 2) throw IllegalArgumentException("zipSize must be at least 2")

    val seenCombinations = mutableSetOf<List<Int>>()

    fun combine(start: Int, current: MutableList<Pair<T, Int>>) {
        if (current.size == zipSize) {
            val indices = current.map { it.second }.sorted()
            if (indices !in seenCombinations) {
                seenCombinations.add(indices)
            }
            return
        }
        for (i in start until this.size) {
            current.add(Pair(this[i], i))
            combine(i + 1, current)
            current.removeAt(current.lastIndex)
        }
    }

    combine(0, mutableListOf())
    return seenCombinations.map { indices -> indices.map { this[it] } }
}


fun String.after(char: Char) = this.substringAfter(char)
fun String.before(char: Char) = this.substringBefore(char)
fun String.after(str: String) = this.substringAfter(str)
fun String.before(str: String) = this.substringBefore(str)
fun String.after(regex: Regex) = this.substringAfter(regex.find(this)?.value ?: "")
fun String.before(regex: Regex) = this.substringBefore(regex.find(this)?.value ?: "")
fun String.afterLast(char: Char) = this.substringAfterLast(char)
fun String.beforeLast(char: Char) = this.substringBeforeLast(char)
fun String.afterLast(str: String) = this.substringAfterLast(str)
fun String.beforeLast(str: String) = this.substringBeforeLast(str)
fun String.afterLast(regex: Regex) = this.substringAfterLast(regex.find(this)?.value ?: "")
fun String.beforeLast(regex: Regex) = this.substringBeforeLast(regex.find(this)?.value ?: "")
fun String.dropBrackets() = this.replace(Regex("[\\[\\](){}]"), "")
fun String.inv(): String {
    if ("[^0-1]+".toRegex() in this) throw IllegalArgumentException("String must be binary")
    return this.map { if (it == '0') '1' else '0' }.join()
}

fun die(): Nothing = throw RuntimeException("womp womp")
fun <T> List<T>.toPair(): Pair<T, T> {
    require(this.size <= 2) { "List contains more than 2 elements" }
    return this.first() to this.getOrNull(1)!!
}

infix fun String.matching(other: String): String = this.zip(other).filter { (a, b) -> a == b }.map { it.first }.joinToString("")

infix fun String.nonmatching(other: String): String = this.zip(other).filter { (a, b) -> a != b }.map { it.first }.joinToString("")

fun String.halve() = this.splitAt(this.l / 2)
fun String.split() = this.split(" ").dropBlanks()

@JvmName("CollectionStringDropBlanks")
fun Collection<String>.dropBlanks() = this.filter { it.isNotBlank() }

@JvmName("CollectionCollectionDropBlanks")
fun Collection<Collection<*>>.dropBlanks() = this.filter { it.isNotEmpty() }
fun List<String>.split() = this.map { it.split() }

val String.isInt
    get() = try {
        this.toInt(); true
    } catch (e: NumberFormatException) {
        false
    }
val String.isLong
    get() = try {
        this.toLong(); true
    } catch (e: NumberFormatException) {
        false
    }

fun String.toIntOrElse(action: () -> Int) = this.toIntOrNull() ?: action()
fun String.toLongOrElse(action: () -> Long) = this.toLongOrNull() ?: action()
val Int.kilo get() = this * 1_000
val Int.mega get() = this * 1_000_000
val Int.giga get() = this * 1_000_000_000
val Long.kilo get() = this * 1_000
val Long.mega get() = this * 1_000_000
val Long.giga get() = this * 1_000_000_000
val Long.tera get() = this * 1_000_000_000_000
val Long.peta get() = this * 1_000_000_000_000_000
val Long.exa get() = this * 1_000_000_000_000_000_000
operator fun IntRange.plus(other: Int) = (this.first + other)..(this.last + other)
operator fun IntRange.minus(other: Int) = (this.first - other)..(this.last - other)
operator fun LongRange.plus(other: Long) = (this.first + other)..(this.last + other)
operator fun LongRange.minus(other: Long) = (this.first - other)..(this.last - other)
infix fun String.splitAt(index: Int) = Pair(this.substring(0, index), this.substring(index))
infix fun <T, R> Iterable<T>.mp(transform: (T) -> R) = this.map(transform)
fun String.distinct() = this.toSet().joinToString("")
fun String.duplicates(): String = this.groupingBy { it }.eachCount().filter { it.value > 1 }.flatMap { (char, count) -> List(count) { char } }.joinToString("")

fun List<String>.containsLength(length: Int) = this.any { it.l == length }
fun String.consecutive(): List<String> = this.fold(mutableListOf()) { acc, char ->
    if (acc.isEmpty() || acc.last().last() != char) {
        acc.add(char.toString())
    } else {
        acc[acc.lastIndex] = acc.last() + char
    }
    acc
}

fun <T> Collection<T>.swap(i: Int, j: Int): List<T> {
    val c = this.toMutableList()
    c[i] = c[j].also { c[j] = c[i] }
    return c
}

fun <T> Collection<T>.swap(i: T, j: T): List<T> {
    val c = this.toMutableList()
    c[indexOf(i)] = j.also { c[indexOf(j)] = i }
    return c
}

@JvmName("IntListSwap")
fun <T> Collection<T>.swap(e: Pair<Int, Int>): List<T> = this.swap(e.first, e.second)

@JvmName("TListSwap")
fun <T> Collection<T>.swap(e: Pair<T, T>): List<T> = this.swap(e.first, e.second)

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> MutableList<T>.swap(i: T, j: T) {
    val indexI = this.indexOf(i)
    val indexJ = this.indexOf(j)
    if (indexI != -1 && indexJ != -1) {
        this.swap(indexI, indexJ)
    }
}

@JvmName("IntMutListSwap")
fun <T> MutableList<T>.swap(e: Pair<Int, Int>) {
    this.swap(e.first, e.second)
}

@JvmName("TMutListSwap")
fun <T> MutableList<T>.swap(e: Pair<T, T>) {
    this.swap(e.first, e.second)
}


fun <T> List<T>.permutations(): List<List<T>> {
    return if (this.size == 1) listOf(this)
    else this.flatMap { i -> (this - i).permutations().map { listOf(i) + it } }
}

fun String.permutations() = this.toList().permutations().map { it.join() }

fun <T> Collection<T>.modifyAt(index: Int, transform: (T) -> T): List<T> {
    val out = this.toMutableList()
    out[index] = transform(out[index])
    return out
}

fun <T> List<T>.combinations(size: Int): List<List<T>> {
    if (size <= 0) return listOf(emptyList())
    if (size > this.size) return emptyList()
    if (size == this.size) return listOf(this)
    if (size == 1) return this.map { listOf(it) }

    val combinations = mutableListOf<List<T>>()
    val rest = this.drop(1)
    rest.combinations(size - 1).forEach { combination ->
        combinations.add(listOf(this[0]) + combination)
    }
    combinations += rest.combinations(size)
    return combinations
}

fun isqrt(x: Int) = sqrt(x.toDouble()).toInt()
fun File.ril(): List<Int> = this.rl().map { it.toInt() }
fun File.rll(): List<Long> = this.rl().map { it.toLong() }
fun File.rit(): List<Int> = this.rt().split(',', ' ', '-').map { it.toInt() }
fun File.rlt(): List<Long> = this.rt().split(',', ' ', '-').map { it.toLong() }
fun File.rl(): List<String> = this.readLines().dropLastWhile { it.isBlank() }
fun File.rt(): String = this.readText().trim()
fun File.sdnl() = this.rt().split("\n\n")
fun File.sdanl() = this.rt().split("\n\n").map { it.split("\n") }

@JvmName("doubleListGrid")
fun <T> List<List<T>>.grid() = Grid.of(this)

@JvmName("stringListGrid")
fun List<String>.grid() = Grid.of(this)
fun File.intgrid() = this.rl().map { line -> line.map { it.digitToInt() } }.grid()
fun File.longgrid() = this.rl().map { line -> line.map { it.digitToInt().toLong() } }.grid()
fun File.chargrid() = this.rl().map { it.toList() }.grid()
fun String.isLowercase() = this == this.lowercase()
fun String.isUppercase() = this == this.uppercase()
fun List<String>.snl() = this.map { it.split("\n") }
fun List<String>.swd() = this.dropBlanks().filter { it.first().isDigit() }
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

operator fun <T> Collection<T>.get(range: IntRange): List<T> = this.toList().subList(range.first, range.last + 1)
operator fun <T> MutableList<T>.set(range: IntRange, value: T) {
    range.forEach { this[it] = value }
}

val String.l: Int get() = this.length
val Collection<*>.s: Int get() = this.size

fun <T> Collection<T>.destructure() = this.first() to this.drop(1)

fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    return gcd(b, a % b)
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun gcd(a: BigInteger, b: BigInteger): BigInteger {
    if (b == BigInteger.ZERO) return a
    return gcd(b, a % b)
}

fun lcm(a: Int, b: Int): Int {
    return abs(a * b) / gcd(a, b)
}

fun lcm(a: Long, b: Long): Long {
    return abs(a * b) / gcd(a, b)
}

fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return a.multiply(b).divide(a.gcd(b))
}

fun lcm(vararg nums: Int): Int {
    return nums.reduce { a, b -> lcm(a, b) }
}

fun lcm(nums: Collection<Int>): Int {
    return nums.reduce { a, b -> lcm(a, b) }
}

fun lcm(vararg nums: Long): Long {
    return nums.reduce { a, b -> lcm(a, b) }
}

fun lcm(nums: Collection<Long>): Long {
    return nums.reduce { a, b -> lcm(a, b) }
}

fun lcm(vararg nums: BigInteger): BigInteger {
    return nums.reduce { a, b -> lcm(a, b) }
}

fun lcm(nums: Collection<BigInteger>): BigInteger {
    return nums.reduce { a, b -> lcm(a, b) }
}

fun String.remove(vararg strings: String): String {
    var str = this
    for (s in strings) {
        str = str.replace(s, "")
    }
    return str
}

fun String.remove(vararg chars: Char): String {
    var str = this
    for (c in chars) {
        str = str.replace(c.toString(), "")
    }
    return str
}

fun String.remove(vararg regex: Regex): String {
    var str = this
    for (r in regex) {
        str = str.replace(r, "")
    }
    return str
}

@JvmName("removeListString")
fun String.remove(strings: List<String>): String {
    var str = this
    for (s in strings) {
        str = str.replace(s, "")
    }
    return str
}

@JvmName("removeListChar")
fun String.remove(chars: List<Char>): String {
    var str = this
    for (c in chars) {
        str = str.replace(c.toString(), "")
    }
    return str
}

@JvmName("removeListRegex")
fun String.remove(regex: List<Regex>): String {
    var str = this
    for (r in regex) {
        str = str.replace(r, "")
    }
    return str
}

fun String.quantum(replacee: String, vararg replacer: String) = replacer.map { this.replace(replacee, it) }
fun String.quantum(replacee: Char, vararg replacer: Char) = replacer.map { this.replace(replacee, it) }
fun String.quantum(replacee: Regex, vararg replacer: String) = replacer.map { this.replace(replacee, it) }
fun String.quantum(replacee: Regex, vararg replacer: Char) = replacer.map { this.replace(replacee, it.toString()) }
fun String.uniques(): Int = distinct().count()
fun String.counts(): Map<Char, Int> = groupingBy { it }.eachCount()
fun <T> Iterable<T>.uniques() = distinct().count()
fun <T> Iterable<T>.counts() = groupingBy { it }.eachCount()
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

operator fun <T> List<T>.component6(): T {
    return this[5]
}

/**
 * Returns the smallest number that satisfies the given modular congruences according to the chinese remainder theorem.
 * @param mod The list of moduli.
 * @param rem The list of remainders (moduli - offset).
 * @return The chinese remainder.
 */
@JvmName("chineseRemainderIntList")
fun chineseRemainder(mod: List<Int>, rem: List<Int>): Long {
    return chineseRemainder(mod.map { it.toLong() }, rem.map { it.toLong() })
}

/**
 * Returns the smallest number that satisfies the given modular congruences according to the chinese remainder theorem.
 * @param mod The list of moduli.
 * @param rem The list of remainders (moduli - offset).
 * @return The chinese remainder.
 */
fun chineseRemainder(mod: List<Long>, rem: List<Long>): Long {
    val prod = mod.fold(1L) { acc, i -> acc * i }

    return mod.zip(rem).sumOf { (moduli, remainder) ->
        val p = prod / moduli
        remainder * modularMultiplicativeInverse(p, moduli) * p
    }.mod(prod)
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

/**
 * Returns the smallest number that satisfies the given modular congruences according to the chinese remainder theorem.
 * @param mod The list of moduli.
 * @param rem The list of remainders (moduli - offset).
 * @return The chinese remainder.
 */
fun chineseRemainder(mod: List<BigInteger>, rem: List<BigInteger>): BigInteger {
    val prod = mod.fold(BigInteger.ONE) { acc, i -> acc * i }

    return mod.zip(rem).sumOf { (moduli, remainder) ->
        val p = prod / moduli
        remainder * modularMultiplicativeInverse(p, moduli) * p
    }.mod(prod)
}

fun modularMultiplicativeInverse(a: BigInteger, m: BigInteger): BigInteger {
    var m0 = m
    var x0 = BigInteger.ZERO
    var x1 = BigInteger.ONE

    if (m0 == BigInteger.ONE) return BigInteger.ZERO

    var aTemp = a
    while (aTemp > BigInteger.ONE) {
        if (m0 == BigInteger.ZERO) {
            println("Inverse doesn't exist")
            return BigInteger.ZERO
        }
        val q = aTemp / m0
        var t = m0

        m0 = aTemp % m0
        aTemp = t
        t = x0

        x0 = x1 - q * x0
        x1 = t
    }

    if (x1 < BigInteger.ZERO) {
        x1 += m
    }

    return x1
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

@JvmName("getPolygonAreaInt")
fun getPolygonArea(vertices: List<Point>): Long {
    if (vertices.size < 3) return 0L
    var sum1 = 0L
    var sum2 = 0L

    for (i in vertices.indices) {
        val current = vertices[i]
        val next = vertices[(i + 1) % vertices.size]

        sum1 += current.x * next.y
        sum2 += current.y * next.x
    }

    return abs(sum1 - sum2) / 2
}

@JvmName("getPolygonAreaLong")
fun getPolygonArea(vertices: List<LongPoint>): Long {
    if (vertices.size < 3) return 0L
    var sum1 = 0L
    var sum2 = 0L

    for (i in vertices.indices) {
        val current = vertices[i]
        val next = vertices[(i + 1) % vertices.size]

        sum1 += current.x * next.y
        sum2 += current.y * next.x
    }

    return abs(sum1 - sum2) / 2
}

infix fun <T> T.log(meta: Any?): T = this.also { println("$meta    $it") }

fun min(a: Point, b: Point) = if(a < b) a else b
fun max(a: Point, b: Point) = if(a > b) a else b

@Deprecated("Use digitToInt() instead", ReplaceWith("digitToInt()"))
fun Char.asInt() = this.toString().toInt()

@Deprecated("Use digitToChar() instead", ReplaceWith("digitToChar()"))
fun Int.asChar() = this.toString().first()