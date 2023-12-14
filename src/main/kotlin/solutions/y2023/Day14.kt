package solutions.y2023

import Solution
import utils.Utils.rl
import utils.movement.Direction
import java.io.File

class Day14 : Solution(2023) {
    
    override fun solvePart1(input: File): Any {
        var grid = input.rl().map { it.toCharArray() }
        grid = moveRocks(grid, Direction.NORTH)
        return grid.withIndex().sumOf { (i, row) ->
            row.count { it == 'O' } * (grid.size - i)
        }
    }

    override fun solvePart2(input: File): Any {
        var grid = input.rl().map { it.toCharArray() }
        val seen = mutableMapOf<List<String>, Int>()
        var cycle = 0
        val total = 1_000_000_000
        var length = 0

        while (cycle < total) {
            val cur = grid.map { it.concatToString() }
            if (cur in seen) {
                length = cycle - seen[cur]!!
                break
            }
            seen[cur] = cycle

            grid = moveRocks(grid, Direction.NORTH)
            grid = moveRocks(grid, Direction.WEST)
            grid = moveRocks(grid, Direction.SOUTH)
            grid = moveRocks(grid, Direction.EAST)
            cycle++
        }

        if (length > 0) {
            val remainingCycles = (total - cycle) % length
            for (i in 0 until remainingCycles) {
                grid = moveRocks(grid, Direction.NORTH)
                grid = moveRocks(grid, Direction.WEST)
                grid = moveRocks(grid, Direction.SOUTH)
                grid = moveRocks(grid, Direction.EAST)
            }
        }

        return grid.withIndex().sumOf { (i, row) ->
            row.count { it == 'O' } * (grid.size - i)
        }
    }

    private fun moveRocks(grid: List<CharArray>, direction: Direction): List<CharArray> {
        val n = grid.size
        val m = grid[0].size
        val new = Array(n) { CharArray(m) { '.' } }

        when (direction) {
            Direction.NORTH -> {
                for (j in 0 until m) {
                    var index = 0
                    for (i in 0 until n) {
                        if (grid[i][j] == 'O') {
                            new[index++][j] = 'O'
                        } else if (grid[i][j] == '#') {
                            new[i][j] = '#'
                            index = i + 1
                        }
                    }
                }
            }
            Direction.WEST -> {
                for (i in 0 until n) {
                    var index = 0
                    for (j in 0 until m) {
                        if (grid[i][j] == 'O') {
                            new[i][index++] = 'O'
                        } else if (grid[i][j] == '#') {
                            new[i][j] = '#'
                            index = j + 1
                        }
                    }
                }
            }
            Direction.SOUTH -> {
                for (j in 0 until m) {
                    var index = n - 1
                    for (i in n - 1 downTo 0) {
                        if (grid[i][j] == 'O') {
                            new[index--][j] = 'O'
                        } else if (grid[i][j] == '#') {
                            new[i][j] = '#'
                            index = i - 1
                        }
                    }
                }
            }
            Direction.EAST -> {
                for (i in 0 until n) {
                    var index = m - 1
                    for (j in m - 1 downTo 0) {
                        if (grid[i][j] == 'O') {
                            new[i][index--] = 'O'
                        } else if (grid[i][j] == '#') {
                            new[i][j] = '#'
                            index = j - 1
                        }
                    }
                }
            }
        }
        return new.toList()
    }
}