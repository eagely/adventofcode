package solutions.y2019

import Solution
import utils.extractNegativesSeparated
import utils.lcm
import utils.lines
import java.io.File
import kotlin.math.absoluteValue

class Day12 : Solution(2019) {

    data class Moon(var p: MutableList<Int>, val v: MutableList<Int>)

    override fun solvePart1(input: File) = solve(input, false)

    override fun solvePart2(input: File) = solve(input, true)

    private fun solve(input: File, wantRepeat: Boolean): Number {
        val moons = input.lines.map { Moon(it.extractNegativesSeparated().toMutableList(), mutableListOf(0, 0, 0)) }
        val seen = MutableList(3) { hashSetOf<List<Pair<Int, Int>>>() }
        val repeats = mutableMapOf<Int, Int>()
        var i = 0
        while (true) {
            for (m in moons) {
                for (n in moons) {
                    if (m == n) continue
                    for (d in 0..2) m.v[d] += if (m.p[d] > n.p[d]) -1 else if (m.p[d] < n.p[d]) 1 else 0
                }
            }
            for (moon in moons) moon.p = moon.p.zip(moon.v).map { it.first + it.second }.toMutableList()

            for (d in 0..2) if (!seen[d].add(moons.map { it.p[d] to it.v[d] }) && d !in repeats) repeats[d] = i
            i++
            if (repeats.size == 3) break
            if (!wantRepeat && i == 1000) return moons.sumOf { moon -> moon.p.sumOf { it.absoluteValue } * moon.v.sumOf { it.absoluteValue } }
        }
        return lcm(repeats.values)
    }
}
