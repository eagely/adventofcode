package solutions.y2018

import Solution
import utils.*
import java.io.File

class Day24 : Solution(2018) {

    private enum class Type {
        SLASHING, RADIATION, COLD, FIRE, BLUDGEONING;

        companion object {
            fun of(str: String): Type {
                return entries.first { it.name.equals(str, true) }
            }
        }
    }

    private enum class Side {
        IMMUNE, INFECTION
    }

    private data class Army(
        var units: Int,
        var hp: Int,
        var power: Int,
        val damageType: Type,
        val immune: List<Type>,
        val weak: List<Type>,
        val initiative: Int,
        val side: Side
    )

    private fun battle(input: List<Army>): Pair<Boolean, Int> {
        val targets = HashMap<Int, Int>()
        var armies = input
        var prev = 0

        while (!armies.map { it.side }.isAllEqual()) {
            for (army in armies.filter { it.units > 0 }
                .sortedWith(compareByDescending<Army> { it.units * it.power }.thenByDescending { it.initiative })) {
                val enemies = armies.filter { it.units > 0 && it.side != army.side }
                    .sortedWith(compareByDescending<Army> { it.units * it.power }.thenByDescending { it.initiative })
                if (enemies.all { army.damageType in it.immune }) continue
                val target = enemies
                    .filter { it.initiative !in targets.values }
                    .map { enemy ->
                        val damageMultiplier = when (army.damageType) {
                            in enemy.weak -> 2
                            in enemy.immune -> 0
                            else -> 1
                        }
                        Pair(enemy, army.power * army.units * damageMultiplier)
                    }
                    .filter { it.second > 0 }
                    .sortedWith(
                        compareByDescending<Pair<Army, Int>> { it.second }
                            .thenByDescending { it.first.power * it.first.units }
                            .thenByDescending { it.first.initiative }
                    )
                    .firstOrNull()?.first
                    ?: continue


                targets[army.initiative] = target.initiative
            }

            for ((ai, di) in targets.filter { (k, v) -> armies.any { it.initiative == k } && armies.any { it.initiative == v } }
                .toList().sortedByDescending { it.first }) {
                if (armies.map { it.side }.isAllEqual()) die()
                val attacker = armies.first { it.initiative == ai }
                val defender = armies.first { it.initiative == di }

                if (defender.units <= 0) die()
                if (attacker.units <= 0) continue

                val damage = attacker.units * attacker.power * when (attacker.damageType) {
                    in defender.weak -> 2
                    in defender.immune -> 0
                    else -> 1
                }

                defender.units -= damage / defender.hp
            }

            targets.clear()
            armies = armies.filter { it.units > 0 }
            if (armies.sumOf { it.units } == prev) return false to armies.sumOf { it.units }
            prev = armies.sumOf { it.units }
        }
        return (armies.map { it.side }.first() == Side.IMMUNE) to armies.sumOf { it.units }
    }

    override fun solvePart1(input: File) = battle(parse(input)).second

    override fun solvePart2(input: File): Any {
        var boost = 0
        while (true) {
            val army = parse(input)

            army.forEach {
                if (it.side == Side.IMMUNE)
                    it.power += boost
            }

            val (r, v) = battle(army)
            if (r) return v
            boost++
        }
    }

    private fun parse(input: File): List<Army> {
        val (immune, infection) = input.sdanl().map { line ->
            val type = line.first().before(' ').before(':').lowercase()
            line.drop(1).map { army ->
                val numbers = army.extractNumbersSeparated()
                Army(
                    numbers[0],
                    numbers[1],
                    numbers[2],
                    Type.of(army.before(" damage").afterLast(' ')),
                    if ("immune to " in army) army.after("immune to ").before(';').before(')').split(", ")
                        .map { Type.of(it) } else emptyList(),
                    if ("weak to " in army) army.after("weak to ").before(';').before(')').split(", ")
                        .map { Type.of(it) } else emptyList(),
                    numbers[3],
                    if (type == "immune") Side.IMMUNE else Side.INFECTION
                )
            }
        }

        return immune + infection
    }
}