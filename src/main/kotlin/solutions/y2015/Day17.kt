package solutions.y2015

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest
import kotlin.math.min

class Day17 : Solution(2015) {

    @NoTest
    override fun solvePart1(input: File) = fill(150, input.ints).first

    @NoTest
    override fun solvePart2(input: File) = input.ints.let { fill(150, it, fill(150, it).second).second }

    private fun fill(capacity: Int, containers: List<Int>, allowed: Int = containers.size): Pair<Int, Int> {
        val n = containers.size
        var c = 0
        var min = Int.MAX_VALUE

        for (i in 0..(1 shl n)) {
            var s = 0
            for (j in 0..<n) {
                if (i and (1 shl j) != 0) {
                    s += containers[j]
                }
            }
            if (s == capacity && i.countOneBits() <= allowed) {
                c++
                min = min(min, i.countOneBits())
            }
        }

        return c to min
    }
}