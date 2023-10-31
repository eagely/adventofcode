package solutions.y2022

import Solution
import utils.Utils.extractNegatives
import utils.grid.Grid
import utils.point.Point
import java.io.File


class Day15 : Solution(2022) {
    override fun solvePart1(input: File): String {
        val inputLines = input.readLines()

        val map = inputLines.map { line ->
            line.split(",", ":").map { s -> s.extractNegatives() }.let { Point(it[0].toInt(), it[1].toInt()) to Point(it[2].toInt(), it[3].toInt()) }
        }

        val grid = Grid<Char>()
        val sensors = mutableMapOf<Point, Point>()

        for ((sensor, beacon) in map) {
            grid[sensor] = 'S'
            grid[beacon] = 'B'
            sensors[sensor] = beacon
        }

        sensors.forEach { (sensor, beacon) ->
            grid.add(sensor.getCloserOrEqualPointsForRow(beacon, 2000000), '#')
        }

        return grid.getColumn(2000000).count { it == '#' || it == 'S' }.toString()
    }

    private fun Point.getCloserOrEqualPointsForRow(beacon: Point, row: Int): Set<Point> {
        return (x - this.manhattanDistance(beacon) .. x + this.manhattanDistance(beacon)).mapNotNull { dx ->
            Point(dx, row).takeIf { it.manhattanDistance(this) <= this.manhattanDistance(beacon) }
        }.toSet()
    }

    override fun solvePart2(input: File): String {
        val lines = input.readLines()
        val sensorSet = mutableSetOf<SensorData>()

        for (line in lines) {
            val parts = line.split(",", ":").map { it.extractNegatives().toInt() }
            val locPoint = Point(parts[0], parts[1])
            val beaconPoint = Point(parts[2], parts[3])
            val distToBeacon = locPoint.manhattanDistance(beaconPoint)
            sensorSet.add(SensorData(locPoint, beaconPoint, distToBeacon))
        }

        val validCaveRange = 0..4000000

        for (sensor in sensorSet) {
            val borderPts = sensor.loc.calculateBorderPoints(sensor.dist)
            for (point in borderPts) {
                if (point.x in validCaveRange && point.y in validCaveRange) {
                    if (sensorSet.none { it.loc.manhattanDistance(point) <= it.dist }) {
                        return (point.x * 4000000L + point.y).toString()
                    }
                }
            }
        }

        return "Distress beacon not found."
    }

    private fun Point.calculateBorderPoints(distance: Int): List<Point> {
        val up = Point(x, y - distance - 1)
        val down = Point(x, y + distance + 1)
        val left = Point(x - distance - 1, y)
        val right = Point(x + distance + 1, y)
        return up.lineTo(right) + right.lineTo(down) + down.lineTo(left) + left.lineTo(up)
    }
    data class SensorData(val loc: Point, val nearestBeacon: Point, val dist: Int)
}