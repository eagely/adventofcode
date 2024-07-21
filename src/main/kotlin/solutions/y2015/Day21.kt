package solutions.y2015

import Solution
import java.io.File
import utils.*
import kotlin.math.max
import kotlin.math.min

class Day21 : Solution(2015) {

    data class Item(val price: Int, val damage: Int, val armor: Int)

    private val weaponShop = setOf(
        Item(8, 4, 0),
        Item(10, 5, 0),
        Item(25, 6, 0),
        Item(40, 7, 0),
        Item(74, 8, 0)
    )

    private val armorShop = setOf(
        Item(13, 0, 1),
        Item(31, 0, 2),
        Item(53, 0, 3),
        Item(75, 0, 4),
        Item(102, 0, 5),
        Item(0, 0, 0)
    )

    private val ringShop = listOf(
        Item(25, 1, 0),
        Item(50, 2, 0),
        Item(100, 3, 0),
        Item(20, 0, 1),
        Item(40, 0, 2),
        Item(80, 0, 3)
    )

    data class Entity(var hp: Int, val damage: Int, val armor: Int)

    override fun solvePart1(input: File): Any {
        val boss = input.text.extractNumbersSeparated().let { Entity(it[0], it[1], it[2]) }
        var min = Int.MAX_VALUE
        for (weapon in weaponShop)
            for (armor in armorShop)
                for (ring in ringShop.zipWithAllUnique().map { (a, b) -> Item(a.price + b.price, a.damage + b.damage, a.armor + b.armor) } + ringShop)
                    if (fight(Entity(100, weapon.damage + ring.damage, armor.armor + ring.armor), boss))
                        min = min(min, weapon.price + armor.price + ring.price)

        return min
    }

    override fun solvePart2(input: File): Any {
        val boss = input.text.extractNumbersSeparated().let { Entity(it[0], it[1], it[2]) }
        var max = 0
        for (weapon in weaponShop)
            for (armor in armorShop)
                for (ring in ringShop.zipWithAllUnique().map { (a, b) -> Item(a.price + b.price, a.damage + b.damage, a.armor + b.armor) } + ringShop)
                    if (!fight(Entity(100, weapon.damage + ring.damage, armor.armor + ring.armor), boss))
                        max = max(max, weapon.price + armor.price + ring.price)
        return max
    }

    private fun fight(player: Entity, boss: Entity): Boolean {
        var yourTurn = true
        boss.hp = 100
        while (player.hp > 0) {
            if (yourTurn) boss.hp -= max(player.damage - boss.armor, 1)
            else player.hp -= max(boss.damage - player.armor, 1)
            yourTurn = !yourTurn
            if (boss.hp <= 0) return true
        }
        return false
    }
}