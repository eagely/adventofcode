package solutions.y2023

import Solution
import utils.Utils.afterLast
import utils.Utils.dropBrackets
import utils.Utils.sdnl
import java.io.File

class Day19 : Solution(2023) {

    data class Blob(val key: Char, val cmp: Char, val n: Int, val go: String)

    override fun solvePart1(input: File): Any {
        val (blob, plop) = input.sdnl()
        val workflows = parse(blob)
        fun isBased(item: Map<Char, Int>, name: String = "in"): Boolean {
            if (name == "R") return false
            if (name == "A") return true
            for ((key, cmp, n, target) in workflows[name]!!.first) {
                when (cmp) {
                    '>' -> if (item[key]!! > n) return isBased(item, target)
                    '<' -> if (item[key]!! < n) return isBased(item, target)
                }
            }
            return isBased(item, workflows[name]!!.second)
        }
        return plop.lines().sumOf { line ->
            line.dropBrackets().split(",").associate {
                val (ch, n) = it.split("=")
                ch[0] to n.toInt()
            }.let { if (isBased(it)) it.values.sum() else 0 }
        }
    }

    override fun solvePart2(input: File): Any {
        val workflows = parse(input.sdnl().first())
        fun count(ranges: Map<Char, IntRange>, name: String = "in"): Long {
            if (name == "R") return 0
            if (name == "A") return ranges.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
            val (rules, fallback) = workflows[name]!!
            var total = 0L
            val nr = ranges.toMutableMap()
            for ((key, cmp, n, target) in rules) {
                val range = nr[key]!!
                val (a, r) = if (cmp == '>') n + 1..range.last to range.first..n else range.first..<n to n..range.last
                if (a.first <= a.last) total += count(nr.apply { put(key, a) }, target)
                if (r.first <= r.last) nr[key] = r else break
            }
            return total + count(nr, fallback)
        }
        return count("xmas".associate { it to (1..4000) })
    }

    private fun parse(input: String): HashMap<String, Pair<List<Blob>, String>> {
        val blobset = hashMapOf<String, Pair<List<Blob>, String>>()
        input.lines().forEach { line ->
            val (name, rest) = line.dropLast(1).split("{")
            blobset[name] = Pair(rest.split(",").dropLast(1).map {
                val (c, g) = it.split(":")
                Blob(c.first(), c[1], c.substring(2).toInt(), g)
            }, rest.afterLast(",").split(":").last())
        }
        return blobset
    }
}