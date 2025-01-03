package utils.computing

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import utils.pow

class IntCode(private val program: MutableMap<Long, Long> = mutableMapOf<Long, Long>().withDefault { 0 }, val input: Channel<Long>) {
    constructor(program: IntArray, input: Channel<Long>) : this(
        program.withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap().withDefault { 0 },
        input
    )

    val output = Channel<Long>(Channel.UNLIMITED)
    private var relativeBase = 0L
    private var p = 0L

    fun run() = runBlocking {
        runSuspending()
        output.toList()
    }

    private fun Long.getParameterMode(i: Int) = this / 10.pow(i + 1) % 10

    private fun pos(mode: Long, i: Long) = when (mode) {
        0L -> program.getValue(program.getValue(i))
        1L -> program.getValue(i)
        2L -> program.getValue(relativeBase + program.getValue(i))
        else -> throw IllegalArgumentException("Invalid mode: $mode")
    }

    private fun writePos(mode: Long, i: Long): Long = when (mode) {
        0L -> program.getValue(i)
        2L -> relativeBase + program.getValue(i)
        else -> throw IllegalArgumentException("Invalid mode for write position: $mode")
    }

    private fun param(p: Long, offset: Long) = pos(program.getValue(offset).getParameterMode(p.toInt()), offset + p)

    private val cur get() = program.getValue(p)

    private suspend fun execute() {
        p += when ((cur % 100).toInt()) {
            1 -> {
                val targetPos = writePos(program.getValue(p).getParameterMode(3), p + 3)
                program[targetPos] = param(1, p) + param(2, p)
                4
            }

            2 -> {
                val targetPos = writePos(program.getValue(p).getParameterMode(3), p + 3)
                program[targetPos] = param(1, p) * param(2, p)
                4
            }

            3 -> {
                val targetPos = writePos(program.getValue(p).getParameterMode(1), p + 1)
                program[targetPos] = input.receive()
                2
            }

            4 -> {
                output.send(param(1, p))
                2
            }

            5 -> if (param(1, p) != 0L) param(2, p) - p else 3
            6 -> if (param(1, p) == 0L) param(2, p) - p else 3
            7 -> {
                val targetPos = writePos(program.getValue(p).getParameterMode(3), p + 3)
                program[targetPos] = if (param(1, p) < param(2, p)) 1 else 0
                4
            }

            8 -> {
                val targetPos = writePos(program.getValue(p).getParameterMode(3), p + 3)
                program[targetPos] = if (param(1, p) == param(2, p)) 1 else 0
                4
            }

            9 -> {
                relativeBase += param(1, p)
                2
            }

            99 -> 0
            else -> throw IllegalArgumentException("Invalid LongCode operation: ${program[p]}")
        }
    }

    suspend fun runSuspending() {
        try {
            while (cur != 99L) {
                execute()
            }
        } finally {
            output.close()
        }
    }

    companion object {
        fun <T> List<T>.toChannel(): Channel<T> = Channel<T>(Channel.UNLIMITED).also { channel ->
            forEach { e -> channel.trySend(e) }
        }
    }
}