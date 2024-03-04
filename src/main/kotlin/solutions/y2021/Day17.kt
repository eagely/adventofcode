package solutions.y2021

import Solution
import utils.Utils.rt
import utils.extractNegativesSeparated
import utils.point.Point
import java.io.File
import kotlin.math.max

class Day17 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val (tx, ty) = parse(input)
        var maxy = 0
        for (vx in 0..1000) {
            for (vy in 0..1000) {
                maxy = max(maxy, shoot(vx, vy, tx, ty))
            }
        }
        return maxy
    }

    override fun solvePart2(input: File): Any {
        val (tx, ty) = parse(input)
        var c = 0
        for (vx in 0..1000) {
            for (vy in -1000..1000) {
                if (shoot(vx, vy, tx, ty) != -1) c++
            }
        }
        return c
    }

    private fun shoot(ix: Int, iy: Int, tx: IntRange, ty: IntRange): Int {
        var pos = Point(0, 0)
        var vx = ix
        var vy = iy
        var maxy = 0
        while (pos.x <= tx.last && pos.y >= ty.first) {
            pos += Point(vx, vy)
            if (vx > 0) vx-- else if (vx < 0) vx++
            if (pos.y > maxy) maxy = pos.y
            vy -= 1
            if (pos.x in tx && pos.y in ty) return maxy
        }
        return -1
    }

    private fun parse(input: File) = input.rt().let { it.extractNegativesSeparated().let { it[0]..it[1] to it[2]..it[3] } }
}