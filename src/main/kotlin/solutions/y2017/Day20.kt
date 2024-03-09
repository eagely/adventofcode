package solutions.y2017

import Solution
import java.io.File
import utils.*
import utils.point.LongPoint3D

class Day20 : Solution(2017) {

    data class Particle(var p: LongPoint3D, var v: LongPoint3D, val a: LongPoint3D) {
        fun sim(n: Long) {
            v += a * n
            p += v * n + (a * (n * n - n) / 2)
        }
    }

    override fun solvePart1(input: File): Any {
        val particles = parse(input)
        particles.forEach { it.sim(1L.mega) }
        return particles.indexOf(particles.minBy { it.p.manhattanDistance(LongPoint3D.ORIGIN) })
    }

    override fun solvePart2(input: File): Any {
        val particles = parse(input).toMutableList()
        repeat(100) {
            particles.forEach { it.sim(1) }
            particles.removeAll { p -> particles.any { it != p && it.p == p.p } }
        }
        return particles.size
    }

    private fun parse(input: File) =  input.lines.map { it.extractNegativesSeparated().let { Particle(LongPoint3D(it[0], it[1], it[2]), LongPoint3D(it[3], it[4], it[5]), LongPoint3D(it[6], it[7], it[8])) } }
}