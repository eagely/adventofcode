package solutions.y2019

import Solution
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.computing.IntCode
import utils.text
import java.io.File
import kotlin.math.sign

class Day13 : Solution(2019) {

    override fun solvePart1(input: File) = run(input).first

    override fun solvePart2(input: File): Any {
        val program = parse(input)
        var paddle = run(input).second
        program.program[0] = 2
        var score = 0
        runBlocking {
            launch { program.runSuspending() }
            while (!program.output.isClosedForReceive) {
                val p = program.output.receive().toInt()
                program.output.receive()
                val id = program.output.receive().toInt()
                if (p == -1) score = id
                else when (id) {
                    4 -> program.input.send((p - paddle).sign.toLong())
                    3 -> paddle = p
                }
            }
        }
        return score
    }

    private fun run(input: File): Pair<Int, Int> {
        val program = parse(input)
        var blocks = 0
        var paddle = 0
        runBlocking {
            launch { program.runSuspending() }
            while (!program.output.isClosedForReceive) {
                val p = program.output.receive().toInt()
                program.output.receive()
                when (program.output.receive().toInt()) {
                    2 -> blocks++
                    3 -> paddle = p
                }
            }
        }
        return blocks to paddle
    }

    private fun parse(input: File) = IntCode(input.text.split(',').withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap().withDefault { 0 }, Channel(Channel.UNLIMITED))
}