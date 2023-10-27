package solutions

import Solution
import java.io.File
import java.lang.IndexOutOfBoundsException

class Day2 : Solution(2019) {
    override fun solvePart1(input: File): String {
        var intCode = input.readText().trim().split(",").map { it.toInt() }.toMutableList()
        intCode[1] = 12
        intCode[2] = 2
        for (i in intCode.indices step 4) {
            when (intCode[i]) {
                99 -> break
                1 -> intCode[intCode[i + 3]] = intCode[intCode[i + 1]] + intCode[intCode[i + 2]]
                2 -> intCode[intCode[i + 3]] = intCode[intCode[i + 1]] * intCode[intCode[i + 2]]
            }
        }
        return intCode.first().toString()
    }

    override fun solvePart2(input: File): String {
        var intCode = input.readText().trim().split(",").map { it.toInt() }.toMutableList()
        intCode[1] = 0
        intCode[2] = 0
        for (i in 0..99) {
            for (j in 0..99) {
                intCode = input.readText().trim().split(",").map { it.toInt() }.toMutableList()
                intCode[1] = i
                intCode[2] = j
                inner@ for (k in intCode.indices step 4) {
                    try {
                        when (intCode[k]) {
                            99 -> if (intCode.first() == 19690720) return (100 * i + j).toString()

                            1 -> intCode[intCode[k + 3]] = intCode[intCode[k + 1]] + intCode[intCode[k + 2]]

                            2 -> intCode[intCode[k + 3]] = intCode[intCode[k + 1]] * intCode[intCode[k + 2]]
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        continue@inner
                    }
                }
            }
        }
        return "No solution found"
    }
}