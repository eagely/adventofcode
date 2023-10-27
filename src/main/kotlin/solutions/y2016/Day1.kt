package solutions.y2016

import java.io.File
import Solution
import utils.Utils.abs
import utils.point.Point

class Day1 : Solution(2016) {
    override fun solvePart1(input: File): String {
        val path = input.readText().trim().split(", ")
        var pos = Point(0, 0)
        var direction = 0
        var steps = 0
        for (i in path) {
            when (i.first()) {
                'L' -> direction -= 90
                'R' -> direction += 90
            }
            if (direction == -90)
                direction = 270
            if (direction == 360)
                direction = 0
            steps = i.drop(1).toInt()
            when (direction) {
                0 -> pos += Point(0, steps)
                90 -> pos += Point(steps, 0)
                180 -> pos += Point(0, -steps)
                270 -> pos += Point(-steps, 0)
            }
        }
        return (pos.x.abs() + pos.y.abs()).toString()
    }

    override fun solvePart2(input: File): String {
        val path = input.readText().trim().split(", ")
        var visited = HashSet<Point>()
        var pos = Point(0, 0)
        var direction = 0
        var steps = 0
        for (i in path) {
            when (i.first()) {
                'L' -> direction -= 90
                'R' -> direction += 90
            }
            if (direction == -90)
                direction = 270
            if (direction == 360)
                direction = 0
            steps = i.drop(1).toInt()
            for (j in 0..<steps) {
                when (direction) {
                    0 -> pos += Point(0, 1)
                    90 -> pos += Point(1, 0)
                    180 -> pos += Point(0, -1)
                    270 -> pos += Point(-1, 0)
                }
                if(visited.contains(pos)) {
                    println("Twice: $pos")
                    return (pos.x.abs() + pos.y.abs()).toString()
                } else {
                    println("Visited: $pos")
                    visited.add(pos)
                }
            }

        }
        return ""
    }
}