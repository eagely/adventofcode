package solutions.y2015

import Solution
import java.io.File
import utils.*
import kotlin.math.max

class Day22 : Solution(2015) {

    data class State(var playerHp: Int, var bossHp: Int, val bossDamage: Int, var mana: Int, var poison: Int, var shield: Int, var recharge: Int, var totalMana: Int, var yourTurn: Boolean)
    data class NewState(val playerHp: Int, val bossHp: Int, val manaUsage: Int, val poison: Int, val shield: Int, val recharge: Int)

    override fun solvePart1(input: File): Any {
        val (hp, dmg) = input.text.extractNumbersSeparated().let { it[0] to it[1] }
        val state = State(50, hp, dmg, 500, 0, 0, 0, 0, true)
        return fight(state)
    }

    override fun solvePart2(input: File): Any {
        val (hp, dmg) = input.text.extractNumbersSeparated().let { it[0] to it[1] }
        val state = State(50, hp, dmg, 500, 0, 0, 0, 0, true)
        return fight(state, true)
    }

    private fun fight(initialState: State, hardMode: Boolean = false): Any {
        val cache = hashSetOf<State>()
        val queue = ArrayDeque<State>()

        queue.add(initialState)

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            var (playerHp, bossHp, bossDamage, mana, poison, shield, recharge, totalMana, yourTurn) = cur
            val newStates = mutableListOf<NewState>()

            if (yourTurn && hardMode) playerHp--

            if (cur in cache || playerHp <= 0) continue
            cache.add(cur)

            if (poison > 0) {
                bossHp -= 3
                poison--
            }

            if (recharge > 0) {
                mana += 101
                recharge--
            }

            if (shield > 0) shield--

            if (yourTurn) {
                if (mana < 53 || playerHp <= 0) continue

                newStates.add(NewState(playerHp, bossHp - 4, 53, poison, shield, recharge))

                newStates.add(NewState(playerHp + 2, bossHp - 2, 73, poison, shield, recharge))

                if (shield == 0)
                    newStates.add(NewState(playerHp, bossHp, 113, poison, 6, recharge))

                if (poison == 0)
                    newStates.add(NewState(playerHp, bossHp, 173, 6, shield, recharge))

                if (recharge == 0)
                    newStates.add(NewState(playerHp, bossHp, 229, poison, shield, 5))
            } else {
                if (bossHp <= 0) return totalMana

                newStates.add(NewState(playerHp - (max(bossDamage - if (shield > 0) 7 else 0, 1)), bossHp, 0, poison, shield, recharge))
            }
            queue.addAll(newStates.map { ns -> State(ns.playerHp, ns.bossHp, bossDamage, mana - ns.manaUsage, ns.poison, ns.shield, ns.recharge, totalMana + ns.manaUsage, !yourTurn) })
        }
        return "No solution found, replace the return totalMana in line 72 with a minimum calculation"
    }
}