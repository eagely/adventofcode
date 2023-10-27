package solutions.y2022
import Solution
import java.io.File

class Day3 : Solution(2022) {


    override fun solvePart1(input: File): String {
        val lines: List<String> = input.readLines()
        return sortBackpacks(lines).toString()
    }

    override fun solvePart2(input: File): String {
        val lines: List<String> = input.readLines()
        return sortThreeBackpacks(lines).toString()
    }

    private fun sortBackpacks(input: List<String>): Int {
        var priority = 0

        var first: String
        var second: String
        var sharedItem = ' '

        for (i in input.indices) {
            first = input[i].substring(0, input[i].length / 2)
            second = input[i].substring(input[i].length / 2)
            for (j in first.indices)
                if (second.indexOf(first[j]) != -1) {
                    sharedItem = first[j]
                    break
                }
            priority += if (sharedItem.isUpperCase())
                sharedItem.code - 38
            else
                sharedItem.code - 96
        }
        return priority
    }

    private fun sortThreeBackpacks(input: List<String>): Int {
        var priority = 0
        var first: String
        var second: String
        var third: String
        var sharedItem = ' '
        for(i in input.indices step 3) {
            first = input[i]
            second = input[i + 1]
            third = input[i + 2]
            for (j in first.indices)
                if (second.indexOf(first[j]) != -1 && third.indexOf(first[j]) != -1) {
                    sharedItem = first[j]
                    break
                }
            priority += if (sharedItem.isUpperCase())
                sharedItem.code - 38
            else
                sharedItem.code - 96
        }
        return priority
    }
}
