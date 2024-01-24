package utils.line

import utils.point.Point
import kotlin.math.*

data class Line(var start: Point, var end: Point) {

    val angle: Double get() {
        val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
        return (if (angle < 0) angle + 2 * PI else angle) * 180 / PI
    }

    fun distance(): Double = sqrt(((start.x - end.x).toDouble().pow(2) + (start.y - end.y).toDouble().pow(2)))

    fun manhattanDistance(): Int = abs(start.x - end.x) + abs(start.y - end.y)
    fun getIntegerPoints(x0: Int, y0: Int, x1: Int, y1: Int): List<Point> {
        val points = mutableListOf<Point>()

        var x = x0
        var y = y0
        val dx = Math.abs(x1 - x0)
        val dy = Math.abs(y1 - y0)
        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1
        var err = dx - dy

        while (true) {
            points.add(Point(x, y))

            if (x == x1 && y == y1) {
                break
            }

            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x += sx
            }
            if (e2 < dx) {
                err += dx
                y += sy
            }
        }

        return points
    }

    fun intersection(other: Line): List<Point> {
        if (!this.intersects(other)) return emptyList()
        return getIntegerPoints(this.start.x, this.start.y, this.end.x, this.end.y).intersect(getIntegerPoints(other.start.x, other.start.y, other.end.x, other.end.y)).toList()
    }

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

    fun xRange() = min(start.x, end.x)..max(start.x, end.x)
    fun yRange() = min(start.y, end.y)..max(start.y, end.y)

    infix fun intersects(other: Line): Boolean {
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