package solutions.y2015

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest

class Day19 : Solution(2015) {

    @NoTest
    override fun solvePart1(input: File): Any {
        val (inputMap, molecule) = input.sdnl()
        val map = inputMap.split("\n").map { it.split(" => ") }.groupBy({ it.first() }, { it.last() })


        val cache = hashSetOf<String>()
        for ((key, values) in map) {
            for (x in molecule.indices) {
                if (x + key.length <= molecule.length && molecule.substring(x, x + key.length) == key)
                    cache.addAll(values.map { molecule.substring(0, x) + it + molecule.substring(x + key.length) })
            }
        }
        return cache.size
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        val (inputMap, molecule) = input.sdnl()
        val map = inputMap.split("\n").map { it.split(" => ") }.associate { it.last() to it.first() }

        var cur = molecule
        var s = 0
        while (cur != "e") {
            for ((key, value) in map) {
                for (x in cur.indices) {
                    if (x + key.length <= cur.length && cur.substring(x, x + key.length) == key) {
                        cur = cur.substring(0, x) + value + cur.substring(x + key.length)
                        s++
                    }
                }
            }
        }
        return s
    }
}
