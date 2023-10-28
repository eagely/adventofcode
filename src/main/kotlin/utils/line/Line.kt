package utils.line

import utils.point.Point
import kotlin.math.*

data class Line(var start: Point, var end: Point) {

    fun distance(): Double = sqrt(((start.x - end.x).toDouble().pow(2) + (start.y - end.y).toDouble().pow(2)))

    fun manhattanDistance(): Int = abs(start.x - end.x) + abs(start.y - end.y)

    private fun orientation(p: Point, q: Point, r: Point): Int {
        val value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
        return when {
            value > 0 -> 1
            value < 0 -> 2
            else -> 0
        }
    }

    fun onSegment(p: Point, q: Point, r: Point): Boolean = q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) && q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y)
    fun onSegment(point: Point): Boolean = point.x <= max(start.x, end.x) && point.x >= min(start.x, end.x) && point.y <= max(start.y, end.y) && point.y >= min(start.y, end.y)

    fun intersects(other: Line): Boolean {
        val o1 = orientation(start, end, other.start)
        val o2 = orientation(start, end, other.end)
        val o3 = orientation(other.start, other.end, start)
        val o4 = orientation(other.start, other.end, end)

        if (o1 != o2 && o3 != o4) return true

        if (o1 == 0 && onSegment(start, other.start, end)) return true
        if (o2 == 0 && onSegment(start, other.end, end)) return true
        if (o3 == 0 && onSegment(other.start, start, other.end)) return true
        if (o4 == 0 && onSegment(other.start, end, other.end)) return true

        return false
    }

    fun onInfiniteLine(point: Point): Boolean {
        if (end.x == start.x) {
            return point.x == start.x
        }

        val slope = (end.y - start.y).toDouble() / (end.x - start.x).toDouble()
        val expectedY = (slope * (point.x - start.x)) + start.y

        return point.y.toDouble() == expectedY
    }

    override fun toString(): String = "$start..$end"
}