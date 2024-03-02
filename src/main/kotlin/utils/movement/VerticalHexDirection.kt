package utils.movement

import utils.point.Point
import utils.point.Point3D
import kotlin.math.abs
import kotlin.math.max

enum class VerticalHexDirection(val point: Point) {
    NW(Point(-1, 0)),
    NE(Point(1, -1)),
    N(Point(0, -1)),
    SE(Point(1, 0)),
    SW(Point(-1, 1)),
    S(Point(0, 1));

    companion object {
        fun getNeighbors(point: Point) = entries.map { direction -> point + direction.point }

        fun of(string: String) = when (string) {
            "nw" -> NW
            "ne" -> NE
            "n" -> N
            "se" -> SE
            "sw" -> SW
            "s" -> S
            else -> throw IllegalArgumentException("Invalid direction: $string")
        }

        fun stepsToPoint(start: Point, end: Point): Int {
            // The direct difference in x and y coordinates
            val dx = abs(start.x - end.x)
            val dy = abs(start.y - end.y)

            // If moving directly towards the target is possible in fewer steps, use that
            return if (start.x > end.x && start.y < end.y || start.x < end.x && start.y > end.y) {
                // Movement is diagonal, directly reducing both x and y
                max(dx, dy)
            } else {
                // Movement involves both direct and lateral moves
                dx + dy
            }
        }


        fun offsetToCube(point: Point): Point3D {
            val x = point.x - (point.y - (point.y and 1)) / 2
            val z = point.y
            val y = -x - z
            return Point3D(x, y, z)
        }

        fun cubeDistance(a: Point3D, b: Point3D): Int {
            return maxOf(abs(a.x - b.x), abs(a.y - b.y), abs(a.z - b.z))
        }
    }
}
