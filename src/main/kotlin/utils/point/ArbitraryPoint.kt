package utils.point

import utils.Utils.pm
import kotlin.math.abs

data class ArbitraryPoint(var c: List<Int>) {
    constructor(vararg c: Number) : this(c.map { it.toInt() })

    fun getCardinalNeighbors(): List<ArbitraryPoint> {
        return c.indices.flatMap { dimension ->
            listOf(-1, 1).map { delta ->
                ArbitraryPoint(c.mapIndexed { index, value ->
                    if (index == dimension) value + delta else value
                })
            }
        }
    }


    fun getNeighbors(): List<ArbitraryPoint> {
        val deltas = listOf(-1, 0, 1)
        val combinations = generateCombinations(deltas, c.size)
        return combinations.map { combination ->
            ArbitraryPoint(c.zip(combination).map { it.first + it.second })
        }.filterNot { it.c == this.c }
    }

    private fun generateCombinations(deltas: List<Int>, size: Int): List<List<Int>> {
        return if (size == 1) {
            deltas.map { listOf(it) }
        } else {
            generateCombinations(deltas, size - 1).flatMap { combination ->
                deltas.map { delta -> combination + delta }
            }
        }
    }

    fun manhattanDistance(other: ArbitraryPoint) = c.foldIndexed(0) { idx, acc, i -> acc + abs(i - other.c[idx]) }

    operator fun plus(other: ArbitraryPoint) = ArbitraryPoint(c.mapIndexed { idx, it -> it + other.c[idx] })
    operator fun minus(other: ArbitraryPoint) = ArbitraryPoint(c.mapIndexed { idx, it -> it - other.c[idx] })
    operator fun times(other: ArbitraryPoint) = ArbitraryPoint(c.mapIndexed { idx, it -> it * other.c[idx] })
    operator fun div(other: ArbitraryPoint) = ArbitraryPoint(c.mapIndexed { idx, it -> it / other.c[idx] })
    operator fun rem(other: ArbitraryPoint) = ArbitraryPoint(c.mapIndexed { idx, it -> it pm other.c[idx] })

    override fun toString() = c.joinToString("-")

    companion object {
        fun of(input: String) = ArbitraryPoint(input.split(',', '-', ' ').map { it.trim().toInt() })
    }
}
