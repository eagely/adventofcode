package solutions.y2022

import Solution
import utils.Utils.rl
import java.io.File
import java.math.BigInteger

class Day25 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        return toSnafu(input.rl().sumOf { toDecimal(it) })
    }

    private fun toDecimal(snafu: String): BigInteger {
        var total = BigInteger.ZERO
        var place = BigInteger.ONE
        for (i in snafu.reversed()) {
            total += when (i) {
                '1' -> place
                '2' -> BigInteger.TWO * place
                '-' -> -place
                '=' -> -BigInteger.TWO * place
                else -> BigInteger.ZERO
            }
            place *= 5.toBigInteger()
        }
        return total
    }

    private fun toSnafu(decimal: BigInteger): String {
        var value = decimal
        var snafu = ""
        while (value != BigInteger.ZERO) {
            val mod = value.mod(BigInteger.valueOf(5))
            snafu += when {
                mod == BigInteger.ZERO -> {
                    value /= BigInteger.valueOf(5)
                    "0"
                }

                mod < BigInteger.valueOf(3) -> {
                    value /= BigInteger.valueOf(5)
                    mod
                }

                else -> {
                    value = value / BigInteger.valueOf(5) + BigInteger.ONE
                    if (mod == BigInteger.valueOf(3)) '=' else '-'
                }
            }

        }
        return snafu.reversed()
    }

    override fun solvePart2(input: File): Any {
        return "me when the blender"
    }
}
