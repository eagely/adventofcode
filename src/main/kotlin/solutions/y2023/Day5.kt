package solutions.y2023

import Solution
import utils.Utils.containsNumber
import utils.Utils.extractNumbers
import utils.Utils.minus
import utils.Utils.sdnl
import utils.Utils.snl
import utils.Utils.split
import java.io.File

class Day5 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val lines = input.sdnl().snl().toMutableList().map { it.dropWhile { !it.containsNumber() } }.toMutableList()
        val seeds = input.sdnl().snl().flatten().first().substringAfter(" ").split(" ").map { it.extractNumbers().toLong() }
        return seeds.minOf { seed ->
            var next = seed
            lines.drop(1).forEach { line ->
                line.split().firstOrNull {
                    next in it[1].toLong()..it[2].toLong() - 1 + it[1].toLong()
                }?.let { next = (next - it[1].toLong()) + it[0].toLong() }
            }
            next
        }
    }

    override fun solvePart2(input: File): Any {
        val mapping = input.sdnl().snl().toMutableList().map { it.dropWhile { !it.containsNumber() }.split() }.drop(1).map { it.map { it.map { it.toLong() } } }
        val seeds = input.sdnl().snl().flatten().first().substringAfter(": ").split(" ").map { it.extractNumbers().toLong() }.chunked(2).map { it.first()..<it.first() + it.last() }.sortedBy { it.first }
        return seeds.minOf { seed ->
            var unmapped = mutableListOf(seed)
            val mapped = mutableListOf<LongRange>()
            mapping.forEach { task ->
                task.forEach { subtask ->
                    val result = hashMapOf<LongRange, Boolean>()
                    unmapped.forEach { range ->
                        val source = subtask[1] until subtask[1] + subtask.last()
                        val diff = source.first - subtask.first()
                        when {
                            range.first in source && range.last in source -> result[range - diff] = true
                            range.first in source -> {
                                result[(range.first..source.last) - diff] = true
                                result[(source.last + 1)..range.last] = false
                            }
                            range.last in source -> {
                                result[(source.first..range.last) - diff] = true
                                result[range.first until source.first] = false
                            }
                            source.first in range && source.last in range -> {
                                result[(source.first..source.last) - diff] = true
                                result[(range.first until source.first) - diff] = false
                                result[(source.last + 1)..range.last] = false
                            }
                            else -> result[range] = false
                        }
                    }
                    unmapped = result.filter { !it.value }.keys.toMutableList()
                    mapped.addAll(result.filter { it.value }.keys)
                }
                unmapped.addAll(mapped)
                mapped.clear()
            }
            unmapped.minOf { it.first }
        }
    }
}