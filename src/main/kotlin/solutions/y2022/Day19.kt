package solutions.y2022

import Solution
import utils.Utils.extractNumbers
import java.io.File
import kotlin.math.ceil
import kotlin.math.max

class Day19 : Solution(2022) {

    override fun solvePart1(input: File) = parse(input.readLines()).withIndex().sumOf { (i, bp) -> tryBlueprint(bp, 24) * (i + 1) }

    override fun solvePart2(input: File) = parse(input.readLines().take(3)).fold(1) { acc, bp -> acc * tryBlueprint(bp, 32) }

    private var maxGeodes = 0

    data class Bot(val ore: Int, val clay: Int = 0, val obi: Int = 0) {
        operator fun iterator() = listOf(ore, clay, obi).iterator()
    }

    data class Blueprint(val oreBot: Bot, val clayBot: Bot, val obiBot: Bot, val geodeBot: Bot) {
        operator fun iterator() = listOf(oreBot, clayBot, obiBot, geodeBot).iterator()
    }

    private fun parse(input: List<String>) = input.filterNot(String::isBlank).map {
        val s = it.split(":", ". ", "and").map { n -> n.extractNumbers().toInt() }
        Blueprint(Bot(s[1]), Bot(s[2]), Bot(s[3], s[4]), Bot(s[5], 0, s[6]))
    }

    private fun tryBlueprint(blueprint: Blueprint, time: Int): Int {
        val max = listOf(
            maxOf(blueprint.oreBot.ore, blueprint.clayBot.ore, blueprint.obiBot.ore, blueprint.geodeBot.ore),
            maxOf(blueprint.oreBot.clay, blueprint.clayBot.clay, blueprint.obiBot.clay, blueprint.geodeBot.clay),
            maxOf(blueprint.oreBot.obi, blueprint.clayBot.obi, blueprint.obiBot.obi, blueprint.geodeBot.obi)
        )
        return dfs(blueprint, max, hashMapOf(), time, listOf(1, 0, 0, 0), listOf(0, 0, 0, 0))
    }

    private fun dfs(
        bp: Blueprint,
        maxSpend: List<Int>,
        cache: HashMap<Triple<Int, List<Int>, List<Int>>, Int>,
        time: Int,
        bots: List<Int>,
        amt: List<Int>
    ): Int {
        if (time == 0) return amt[3]
        val key = Triple(time, bots, amt)
        cache[key]?.let { return it }

        var max = amt[3] + bots[3] * time
        for ((botType, recipe) in bp.iterator().withIndex()) {
            if (botType != 3 && bots[botType] >= maxSpend[botType] || max + time * (time + 1) / 2 <= maxGeodes) continue

            var wait = 0
            var shouldContinue = true
            for ((resourceType, resourceAmount) in recipe.iterator().withIndex()) {
                if (resourceAmount == 0) continue
                if (bots[resourceType] == 0) {
                    shouldContinue = false
                    break
                }
                wait = max(wait, ceil((resourceAmount - amt[resourceType]).toDouble() / bots[resourceType]).toInt())
            }

            if (shouldContinue) {
                val remtime = time - wait - 1
                if (remtime <= 0) continue

                val newbots = bots.toMutableList().apply { this[botType] += 1 }
                val newamt = amt.zip(bots) { a, b -> a + b * (wait + 1) }.toMutableList().apply {
                    for ((rtype, ramt) in recipe.iterator().withIndex()) this[rtype] -= ramt
                    for (i in 0 until 3) this[i] = minOf(this[i], maxSpend[i] * remtime)
                }
                max = max(max, dfs(bp, maxSpend, cache, remtime, newbots, newamt))
            }
        }
        return max.also { cache[key] = it }
    }
}
