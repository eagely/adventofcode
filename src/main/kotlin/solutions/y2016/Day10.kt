package solutions.y2016

import Solution
import utils.Utils.product
import utils.after
import utils.annotations.NoTest
import utils.before
import utils.extractNumbersSeparated
import utils.lines
import java.io.File
import kotlin.collections.*

class Day10 : Solution(2016) {

    @NoTest
    override fun solvePart1(input: File) = amazonSimulator(input).first

    @NoTest
    override fun solvePart2(input: File) = amazonSimulator(input).second

    private fun amazonSimulator(input: File): Pair<Int, Int> {
        val lines = input.lines
        val bots = hashMapOf<Int, List<Int>>().withDefault { listOf() }
        val output = hashMapOf<Int, List<Int>>().withDefault { listOf() }
        var specialist: Int? = null
        var product: Int? = null
        lines.filter { it.startsWith("value") }.forEach { line ->
            val (value, bot) = line.extractNumbersSeparated()
            bots[bot] = (bots[bot] ?: mutableListOf()) + value
        }
        while (specialist == null || product == null) {
            lines.filter { it.startsWith("bot") }.forEach { line ->
                val (bot, low, high) = line.extractNumbersSeparated()
                val nums = bots.getValue(bot).sorted()
                if (nums.size == 2) {
                    bots[bot] = listOf()
                    if (17 in nums && 61 in nums) specialist = bot
                    product = listOf(output[0], output[1], output[2]).mapNotNull { it?.firstOrNull() }.takeIf { it.size == 3 }?.product()
                    if ("output" !in line.before("high")) bots[low] = bots.getValue(low) + nums.first() else output[low] = output.getValue(low) + nums.first()
                    if ("output" !in line.after("high")) bots[high] = bots.getValue(high) + nums.last() else output[high] = output.getValue(high) + nums.last()
                }
            }
        }
        return specialist!! to product!!
    }
}