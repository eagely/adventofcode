package solutions.y2022
import Solution
import java.io.File

class Day10 : Solution(2022) {

    override fun solvePart1(input: File) = getSignalStrengthSum(input.readLines()).toString()

    override fun solvePart2(input: File) = drawScreen(input.readLines())

    private fun getSignalStrengthSum(input: List<String>): Int {
        var total = 0
        var cycle = 0
        var x = 1
        val signalStrengths = ArrayList<Int>()

        for (i in input.indices) {
            if (input[i] == "noop") {
                if ((++cycle - 20) % 40 == 0) {
                    signalStrengths.add(x * cycle)
                }
                continue
            }
            if ((++cycle - 20) % 40 == 0)
                signalStrengths.add(x * cycle)

            if ((++cycle - 20) % 40 == 0)
                signalStrengths.add(x * cycle)
            x += input[i].substringAfter(" ").toInt()
        }
        for (i in signalStrengths.indices)
            total += signalStrengths[i]
        return total
    }

    private fun drawScreen(input: List<String>): String {
        var output = ""
        var cycle = 0
        var x = 1

        for (i in input.indices) {
            cycle++
            if (input[i] == "noop") {
                output += drawPixel(cycle, x)
                continue
            }
            output += drawPixel(cycle++, x)
            output += drawPixel(cycle, x)
            x += input[i].substringAfter(" ").toInt()
        }
        return output
    }

    private fun drawPixel(cycle: Int, x: Int): String {
        return if ((cycle - 1) % 40 in x - 1..x + 1)
            if (cycle % 40 != 0) "#" else "#\n"
        else
            if(cycle % 40 != 0) "." else ".\n"
    }
}
