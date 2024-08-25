package solutions.y2018

import Solution
import utils.*
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day10 : Solution(2018) {

    data class Light(val position: Point, val velocity: Point)

    override fun solvePart1(input: File) = catchLights(input.lines).first

    override fun solvePart2(input: File) = catchLights(input.lines).second

    private fun catchLights(input: List<String>): Pair<String, Int>{
        var lights = input.map { it.extractNegativesSeparated().let { nums -> Light(Point(nums[0], nums[1]).invert(), Point(nums[2], nums[3]).invert()) } }
        var steps = 0
        while (true) {
            steps++
            lights = lights.map { (p, v) -> Light(p + v, v) }
            if (lights.all { light -> light.position.getNeighbors().any { neighbor -> neighbor in lights.map { it.position } } })
                return Grid.of(lights.associate { it.position to it.velocity }).map { '#' }.toString() to steps
        }
    }
}