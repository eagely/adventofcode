package solutions.y2022

import Solution
import utils.Utils.extractNumbers
import utils.Utils.rl
import java.io.File
import java.math.BigInteger

class Day21 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        return findRoot(input.rl())
    }

    private fun findRoot(input: List<String>, cur: String = "root"): String {
        val curInput = input.find { it.startsWith(cur) }!!.substringAfter(": ")

        if(cur == "root" && input.first { it.startsWith(cur) }.contains("=")) {
            return "${findRoot(input, curInput.split(" ").first())} == ${findRoot(input, curInput.split(" ").last())}"
        }

        if (curInput.extractNumbers().isNotEmpty()) return curInput.extractNumbers()
        return when (curInput.split(" ")[1]) {
            "+" -> findRoot(input, curInput.split(" + ")[0]).toBigInteger() + findRoot(input, curInput.split(" + ")[1]).toBigInteger()
            "*" -> findRoot(input, curInput.split(" * ")[0]).toBigInteger() * findRoot(input, curInput.split(" * ")[1]).toBigInteger()
            "-" -> findRoot(input, curInput.split(" - ")[0]).toBigInteger() - findRoot(input, curInput.split(" - ")[1]).toBigInteger()
            "/" -> findRoot(input, curInput.split(" / ")[0]).toBigInteger() / findRoot(input, curInput.split(" / ")[1]).toBigInteger()
            else -> curInput.substringAfter(": ")
        }.toString()
    }

    override fun solvePart2(input: File): Any {
        val new = input.readLines().toMutableList()
        val rootIndex = new.indexOfFirst { it.startsWith("root") }
        new[rootIndex] = new[rootIndex].replace("[-*+/]".toRegex(), "=")
        new.indexOfFirst { it.startsWith("humn") }
        return binarySearch(new, getMaxHigh(new))
    }

    private fun getMaxHigh(input: MutableList<String>): BigInteger {
        var high = BigInteger.ONE
        input.updateHumanIndex(high)
        var pre = findRoot(input).substringBefore(" == ").toBigInteger()
        var post = findRoot(input).substringAfter(" == ").toBigInteger()
        var oldSign = (post-pre).signum()
        var newSign = oldSign
        while(oldSign == newSign) {
            oldSign = newSign
            high *= BigInteger.TEN
            input.updateHumanIndex(high)
            pre = findRoot(input).substringBefore(" == ").toBigInteger()
            post = findRoot(input).substringAfter(" == ").toBigInteger()
            newSign = (post-pre).signum()
        }
        return high
    }

    private fun binarySearch(input: MutableList<String>, maxHigh: BigInteger): BigInteger {
        var pre: BigInteger
        var post: BigInteger
        var mid: BigInteger
        var high = maxHigh
        var low = BigInteger.ONE
        val oldHigh = high
        while (low <= high) {
            mid = (high + low) / BigInteger.TWO
            input.updateHumanIndex(mid)

            pre = findRoot(input).substringBefore(" == ").toBigInteger()
            post = findRoot(input).substringAfter(" == ").toBigInteger()
            if (pre == post) {
                return mid
            }
            if (post - pre < BigInteger.ZERO) {
                high = mid - BigInteger.ONE
            } else {
                low = mid + BigInteger.ONE
            }

        }
        low = BigInteger.ONE
        high = oldHigh
        while (low <= high) {
            mid = (high + low) / BigInteger.TWO
            input.updateHumanIndex(mid)
            pre = findRoot(input).substringBefore(" == ").toBigInteger()
            post = findRoot(input).substringAfter(" == ").toBigInteger()
            if (pre == post) {
                return mid
            }

            if (post - pre > BigInteger.ZERO) {
                high = mid - BigInteger.ONE
            } else {
                low = mid + BigInteger.ONE
            }

        }
        throw IllegalStateException("How did we get here?")
    }

    private fun MutableList<String>.updateHumanIndex(value: BigInteger) {
        val humanIndex = this.indexOfFirst { it.startsWith("humn") }
        this[humanIndex] = this[humanIndex].replaceAfter(":", " $value")
    }
}