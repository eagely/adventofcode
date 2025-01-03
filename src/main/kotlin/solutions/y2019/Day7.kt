package solutions.y2019

import Solution
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.annotations.NoTest
import utils.computing.IntCode
import utils.computing.IntCode.Companion.toChannel
import utils.permutations
import utils.text
import java.io.File

class Day7 : Solution(2019) {

    @NoTest
    override fun solvePart1(input: File): Any = runBlocking {
        val program = input.text.split(',').map { it.toInt() }.toIntArray()
        (0L..4L).toList().permutations().maxOf { settings ->
            (0..4).fold(0) { past, id ->
                IntCode(program.copyOf(), listOf(settings[id], past.toLong()).toChannel()).run().first().toInt()
            }
        }
    }

    override fun solvePart2(input: File): Any = runBlocking {
        val program = input.text.split(',').map { it.toInt() }.toIntArray()
        (5L..9L).toList().permutations().maxOf { runAmplified(program, it) }
    }

    private suspend fun runAmplified(program: IntArray, settings: List<Long>): Long = coroutineScope {
        val amplifiers = (0..4).fold(listOf<IntCode>()) { acc, i ->
            acc + IntCode(program.copyOf(), if (i == 0) listOf(settings[i], 0).toChannel() else acc.last().output.andSend(settings[i]))

        }
        val spy = Channel<Long>(Channel.CONFLATED)

        coroutineScope {
            launch {
                for (i in amplifiers.last().output) {
                    spy.send(i)
                    amplifiers.first().input.send(i)
                }
            }
            amplifiers.forEach {
                launch { it.runSuspending() }
            }
        }

        spy.receive()
    }

    private suspend fun <T> Channel<T>.andSend(msg: T): Channel<T> = this.also { send(msg) }
}