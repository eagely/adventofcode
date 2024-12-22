package solutions.y2024

import Solution
import utils.longs
import java.io.File

class Day22 : Solution(2024) {

    override fun solvePart1(input: File) = input.longs.sumOf { it.generatePrices().drop(2000).first() }

    override fun solvePart2(input: File): Any {
        val buyers = input.longs
        val prices = mutableMapOf<List<Int>, Int>().withDefault { 0 }

        buyers.forEach { buyer ->
            val secrets = buyer.generatePrices().take(2000).map { it.toInt() % 10 }.toList()
            val changes = secrets.zipWithNext { a, b -> b - a }
            val seen = HashSet<List<Int>>()

            changes.windowed(4).forEachIndexed { i, p ->
                if (seen.add(p)) prices[p] = prices.getValue(p) + secrets[i+4]
            }
        }

        return prices.maxOf { it.value }
    }

    private fun Long.generatePrices() = generateSequence(this) {
        var x = it xor (it shl 6) and 0xffffff
        x = x xor (x shr 5) and 0xffffff
        x xor (x shl 11) and 0xffffff
    }
}