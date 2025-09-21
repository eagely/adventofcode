package solutions.y2019

import Solution
import utils.arrayDequeOf
import utils.lines
import java.io.File

class Day14 : Solution(2019) {
    data class Element(val amount: Long, val name: String)
    data class Formula(val reqs: List<Element>, val res: Element)

    private fun parse(input: File): Map<String, Formula> = input.lines.associate { line ->
        val (lhs, rhs) = line.split(" => ")
        val reqs = lhs.split(", ").map { part ->
            val (n, name) = part.split(' ')
            Element(n.toLong(), name)
        }
        val (outN, outName) = rhs.split(' ')
        val formula = Formula(reqs, Element(outN.toLong(), outName))
        formula.res.name to formula
    }

    override fun solvePart1(input: File) = oreForFuel(parse(input), 1)

    override fun solvePart2(input: File): Any {
        val formulas = parse(input)
        val oreLimit = 1_000_000_000_000L

        var high = 1L
        while (oreForFuel(formulas, high) <= oreLimit) {
            high = high shl 1
            if (high > oreLimit) {
                high = oreLimit
                break
            }
        }

        var low = 1L
        var best = 0L
        while (low <= high) {
            val mid = (low + high) ushr 1
            val ore = oreForFuel(formulas, mid)
            if (ore <= oreLimit) {
                best = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
        }
        return best
    }

    fun oreForFuel(formulas: Map<String, Formula>, fuel: Long): Long {
        val need = arrayDequeOf("FUEL" to fuel)
        val surplus = HashMap<String, Long>()
        var ore = 0L

        while (need.isNotEmpty()) {
            val (chem, rawNeed) = need.removeFirst()
            if (chem == "ORE") {
                ore += rawNeed
                continue
            }

            var required = rawNeed
            surplus[chem]?.let { have ->
                if (have > 0) {
                    val use = minOf(have, required)
                    required -= use
                    if (use == have) surplus.remove(chem) else surplus[chem] = have - use
                }
            }
            if (required == 0L) continue

            val formula = formulas[chem]!!
            val outputAmount = formula.res.amount
            val batches = (required + outputAmount - 1) / outputAmount
            val leftover = batches * outputAmount - required
            if (leftover > 0) surplus[chem] = surplus.getOrDefault(chem, 0L) + leftover

            for (req in formula.reqs) {
                val qty = req.amount * batches
                if (req.name == "ORE") ore += qty else need.addLast(req.name to qty)
            }
        }
        return ore
    }
}