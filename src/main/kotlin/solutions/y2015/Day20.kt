package solutions.y2015

import Solution
import java.io.File
import utils.*
import kotlin.math.min

class Day20 : Solution(2015) {

    override fun solvePart1(input: File) = findHouse(input.text.toInt(), 1081080, 10)

    override fun solvePart2(input: File) = findHouse(input.text.toInt(), 1081080, 11, 50)

    private fun findHouse(min: Int, limit: Int, giftsPerHouse: Int, houseLimit: Int? = null): Int {
        val houses = MutableList(limit+1) { 0 }
        for (i in 1..limit)
            for (j in i..(houseLimit?.let { min(i * 50, limit) } ?: limit) step i)
                houses[j] += i * giftsPerHouse

        return houses.withIndex().first { it.value >= min }.index
    }
}