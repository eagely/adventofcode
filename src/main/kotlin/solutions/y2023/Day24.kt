package solutions.y2023

import Solution
import com.microsoft.z3.Context
import com.microsoft.z3.Status
import utils.Utils.component6
import utils.Utils.die
import utils.Utils.rl
import utils.Utils.zipWithAllUnique
import utils.annotations.NoTest
import java.io.File
import java.math.BigDecimal

class Day24 : Solution(2023) {

    data class HailstonePart1(val x: BigDecimal, val y: BigDecimal, val dx: BigDecimal, val dy: BigDecimal) {
        val a = dy
        val b = -dx
        val c = dy * x - dx * y
    }

    @NoTest
    override fun solvePart1(input: File): Any {
        val range = BigDecimal("200000000000000")..BigDecimal("400000000000000")
        val hailstones = input.rl().map {
            it.split(" @ ", ", ").map { BigDecimal(it.trim()) }.let { (sx, sy, _, vx, vy) -> HailstonePart1(sx, sy, vx, vy) }
        }

        return hailstones.zipWithAllUnique().count { (f, s) ->
            if (f.a * s.b != f.b * s.a) {
                val x = (f.c * s.b - s.c * f.b) / (f.a * s.b - s.a * f.b)
                val y = (s.c * f.a - f.c * s.a) / (f.a * s.b - s.a * f.b)
                x in range && y in range && listOf(f, s).all { (x - it.x) * it.dx >= BigDecimal.ZERO && (y - it.y) * it.dy >= BigDecimal.ZERO }
            } else false
        }
    }

    override fun solvePart2(input: File): Any {
        val hail = input.rl().map { it.split(" @ ", ", ").map { it.trim().toLong() } }
        val ctx = Context()
        val solver = ctx.mkSolver()
        val mx = ctx.mkRealConst("mx")
        val m = ctx.mkRealConst("m")
        val mz = ctx.mkRealConst("mz")
        val mxv = ctx.mkRealConst("mxv")
        val mv = ctx.mkRealConst("mv")
        val mzv = ctx.mkRealConst("mzv")
        repeat(3) {
            val (sx, sy, sz, sxv, syv, szv) = hail[it]
            val t = ctx.mkRealConst("t$it")
            solver.add(ctx.mkEq(ctx.mkAdd(mx, ctx.mkMul(mxv, t)), ctx.mkAdd(ctx.mkReal(sx.toString()), ctx.mkMul(ctx.mkReal(sxv.toString()), t))))
            solver.add(ctx.mkEq(ctx.mkAdd(m, ctx.mkMul(mv, t)), ctx.mkAdd(ctx.mkReal(sy.toString()), ctx.mkMul(ctx.mkReal(syv.toString()), t))))
            solver.add(ctx.mkEq(ctx.mkAdd(mz, ctx.mkMul(mzv, t)), ctx.mkAdd(ctx.mkReal(sz.toString()), ctx.mkMul(ctx.mkReal(szv.toString()), t))))
        }
        if (solver.check() == Status.SATISFIABLE) {
            val model = solver.model
            val solution = listOf(mx, m, mz).sumOf { model.eval(it, false).toString().toDouble() }
            return solution.toLong()
        }
        die()
    }
}