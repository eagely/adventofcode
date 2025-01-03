package utils.computing

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import utils.pow

class IntCode(private val program: IntArray, val input: Channel<Int>) {

    constructor(program: IntArray, input: Int) : this(program, listOf(input).toChannel())
    constructor(program: IntArray, input: List<Int>) : this(program, input.toChannel())

    val output = Channel<Int>(Channel.UNLIMITED)
    fun run() = runBlocking {
        runSuspending()
        output.toList()
    }

    private fun Int.parameterMode(i: Int) = this / 10.pow(i + 1) % 10

    private fun pos(mode: Int, i: Int) = when (mode) {
        0 -> program[program[i]]
        1 -> program[i]
        else -> throw IllegalArgumentException("Invalid mode: $mode")
    }

    private fun param(p: Int, offset: Int) = pos(program[offset].parameterMode(p), offset + p)

    private suspend fun execute(p: Int, program: IntArray): Int {
        return when (program[p] % 100) {
            1 -> {
                program[program[p + 3]] = param(1, p) + param(2, p)
                4
            }

            2 -> {
                program[program[p + 3]] = param(1, p) * param(2, p)
                4
            }

            3 -> {
                program[program[p + 1]] = input.receive()
                2
            }

            4 -> {
                output.send(param(1, p))
                2
            }

            5 -> if (param(1, p) != 0) param(2, p) - p else 3

            6 -> if (param(1, p) == 0) param(2, p) - p else 3

            7 -> {
                program[program[p + 3]] = if (param(1, p) < param(2, p)) 1 else 0
                4
            }

            8 -> {
                program[program[p + 3]] = if (param(1, p) == param(2, p)) 1 else 0
                4
            }

            99 -> 0

            else -> throw IllegalArgumentException("Invalid IntCode operation: ${program[p]}")
        }
    }

    suspend fun runSuspending() {
        var p = 0
        try {
            while (program[p] != 99) {
                p += execute(p, program)
            }
        } finally {
            output.close()
        }
    }

    companion object {
        fun <T> List<T>.toChannel() = Channel<T>(Channel.UNLIMITED).also { forEach { e -> it.trySend(e) } }
    }
}