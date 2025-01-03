package solutions.y2019

import Solution
import utils.computing.IntCode
import utils.computing.IntCode.Companion.toChannel
import utils.text
import java.io.File

class Day9 : Solution(2019) {

    override fun solvePart1(input: File) =
        IntCode(input.text.split(',').withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap().withDefault { 0 }, listOf(1L).toChannel()).run().last()

    override fun solvePart2(input: File) =
        IntCode(input.text.split(',').withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap().withDefault { 0 }, listOf(2L).toChannel()).run().last()
}