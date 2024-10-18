package solutions.y2018

import Solution
import utils.extractLongsSeparated
import utils.lines
import java.io.File

class Day21 : Solution(2018) {

    override fun solvePart1(input: File): Any {
        var seed = 65536L
        var hash = input.lines[8].extractLongsSeparated().first()
        while (true) {
            hash = hash(seed, hash)
            if (seed < 256) return hash else seed /= 256
        }
    }

    override fun solvePart2(input: File): Any {
        var seed = 65536L
        val key = input.lines[8].extractLongsSeparated().first()
        var hash = key
        var prev = 0L
        val seen = HashSet<Long>()

        while (true) {
            hash = hash(seed, hash)
            if (seed < 256) {
                if (!seen.add(hash)) return prev
                prev = hash
                seed = hash or 65536
                hash = key
            } else {
                seed /= 256
            }
        }
    }

    private fun hash(seed: Long, hash: Long) = (seed % 256 + hash) % 16777216 * 65899 % 16777216
}