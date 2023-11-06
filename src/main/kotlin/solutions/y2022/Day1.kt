package solutions.y2022
import Solution
import java.io.File

class Day1 : Solution(2022) {
    override fun solvePart1(input: File): String {
        val lines: List<String> = input.readLines()
        return getMostNutritiousBackpack(lines).toString()
    }

    override fun solvePart2(input: File): String {
        val lines: List<String> = input.readLines()
        return getThreeMostNutritiousBackpacks(lines).toString()
    }

    private fun getMostNutritiousBackpack(input: List<String>): Int {
        var calories = 0
        var backpack = 0
        for (i in input.indices) {
            if (input[i] != "")
                backpack += Integer.parseInt(input[i])
            else {
                if (backpack > calories)
                    calories = backpack
                backpack = 0
            }
        }
        return calories
    }

    private fun getThreeMostNutritiousBackpacks(input: List<String>): Int {
        var backpack = 0
        var first = 0
        var second = 0
        var third = 0
        for (i in input.indices) {
            if (input[i] != "")
                backpack += Integer.parseInt(input[i])
            else {
                if (backpack > first) first = backpack
                else if (backpack > second) second = backpack
                else if (backpack > third) third = backpack
                backpack = 0
            }
        }
        return first + second + third
    }
}
