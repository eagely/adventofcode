package solutions.y2022

import Solution
import utils.Utils.containsNumber
import utils.Utils.p
import utils.Utils.rl
import utils.Utils.toChar
import utils.geometry.Cuboid
import utils.grid.Grid
import utils.movement.Direction
import utils.movement.Direction.*
import java.io.File

class Day22 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        val map = Grid.of(input.rl().takeWhile { !it.containsNumber() })
        var pos = map.getFirst('.')!!
        var facing = EAST
        val pattern = "\\d+|[RLrl]".toRegex()
        val instr = pattern.findAll(input.rl().last()).map { it.value }.toList()
        instr.forEach {
            if (it.containsNumber()) {
                val steps = it.toInt()
                pos = when (facing) {
                    EAST -> map.walledWrappedMove(pos, 0, steps)
                    WEST -> map.walledWrappedMove(pos, 0, -steps)
                    NORTH -> map.walledWrappedMove(pos, -steps, 0)
                    SOUTH -> map.walledWrappedMove(pos, steps, 0)
                }
            } else {
                facing += Direction.of(it.toChar())
            }
        }
        return getPassword(pos.x, pos.y, facing)
    }

    private fun getPassword(row: Int, col: Int, facing: Direction) = (row + 1) * 1000 + (col + 1) * 4 + (facing - EAST).angle / 90
    private fun getPassword(side: Int, row: Int, col: Int, facing: Direction): Int {
        var r = row
        var c = col
        when (side) {
            1 -> c += 100
            2 -> c += 50
            3 -> {
                r += 50
                c += 50
            }
            4 -> {
                r += 100
                c += 50
            }
            5 -> r += 100
            6 -> r += 150
        }
        return getPassword(r, c, facing)
    }

    override fun solvePart2(input: File): Any {
        val map = Grid.of(input.rl().takeWhile { !it.containsNumber() })
        var pos = map.getFirst('.')!!
        var side = 2
        if(pos.y == 8) return "Solution is not implemented for test case, only real inputs (every real input is the same shape and size, different from the test case)"
        var facing = EAST
        val cubeSize = 50
        val pattern = "\\d+|[RLrl]".toRegex()
        val instr = pattern.findAll(input.rl().last()).map { it.value }.toList()
        val cube = Cuboid.of(
            map.subGrid(pos.x, pos.x + cubeSize - 1, pos.y + cubeSize, pos.y + cubeSize * 2 - 1),
            map.subGrid(pos.x, pos.x + cubeSize - 1, pos.y, pos.y + cubeSize - 1),
            map.subGrid(pos.x + cubeSize * 1, pos.x + cubeSize * 2 - 1, pos.y, pos.y + cubeSize - 1),
            map.subGrid(pos.x + cubeSize * 2, pos.x + cubeSize * 3 - 1, pos.y, pos.y + cubeSize - 1),
            map.subGrid(pos.x + cubeSize * 2, pos.x + cubeSize * 3 - 1, pos.y - cubeSize, pos.y - 1),
            map.subGrid(pos.x + cubeSize * 3, pos.x + cubeSize * 4 - 1, pos.y - cubeSize, pos.y - 1),
        )
        pos = 0 p 0
        instr.forEach {
            if (it.containsNumber()) {
                val steps = it.toInt()
                repeat(steps) {
                    cube.move(side, pos, facing).let { (newSide, newPos, newFacing) ->
                        if (cube[newSide - 1][newPos] != '#') {
                            side = newSide
                            pos = newPos
                            facing = newFacing
                        }
                    }
                }
            } else {
                facing += Direction.of(it.toChar())
            }
        }
        return getPassword(side, pos.x, pos.y, facing)
    }
}
