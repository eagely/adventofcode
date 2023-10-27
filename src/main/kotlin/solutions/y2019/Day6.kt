package solutions.y2019

import Solution
import java.io.File

class Day6 : Solution(2019) {
    override fun solvePart1(input: File): String {
        val orbitMap = input.readLines().map { it.substringAfter(")") to it.substringBefore(")") }.toMap()

        fun countOrbits(planet: String): Int {
            return orbitMap[planet]?.let { countOrbits(it) + 1 } ?: 0
        }

        val totalOrbits = orbitMap.keys.sumBy { countOrbits(it) }

        return totalOrbits.toString()
    }


    override fun solvePart2(input: File): String {
        val orbitMap = input.readLines().map { it.substringAfter(")") to it.substringBefore(")") }.toMap()

        fun pathToCenter(planet: String): List<String> {
            return orbitMap[planet]?.let { listOf(it) + pathToCenter(it) } ?: emptyList()
        }

        val youPath = pathToCenter("YOU")
        val sanPath = pathToCenter("SAN")

        val commonPlanet = youPath.find { it in sanPath }

        val transfers = if (commonPlanet != null) {
            youPath.indexOf(commonPlanet) + sanPath.indexOf(commonPlanet)
        } else {
            0
        }

        return transfers.toString()
    }
}