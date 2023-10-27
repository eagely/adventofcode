//package utils.line
//
//import utils.Utils.abs
//import utils.Utils.pow
//import utils.point.Point
//import java.math.BigDecimal
//import java.math.MathContext
//import java.math.RoundingMode
//
//data class Line(var start: Point, var end: Point) {
//
//    fun distance(): BigDecimal =
//        ((start.x - end.x).pow(2) + (start.y - end.y).pow(2)).sqrt(MathContext(32, RoundingMode.HALF_EVEN))
//
//    fun manhattanDistance(): BigDecimal = (start.x - end.x).abs() + (start.y - end.y).abs()
////
//    private fun orientation(p: Point, q: Point, r: Point): Int {
//        val value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
//        return when {
//            value > BigDecimal.ZERO -> 1
//            value < BigDecimal.ZERO -> 2
//            else -> 0
//        }
//    }
//
//    fun onSegment(p: Point, q: Point, r: Point): Boolean = q.x <= p.x.max(r.x) && q.x >= p.x.min(r.x) && q.y <= p.y.max(r.y) && q.y >= p.y.min(r.y)
//    fun onSegment(point: Point): Boolean = point.x <= start.x.max(end.x) && point.x >= start.x.min(end.x) && point.y <= start.y.max(end.y) && point.y >= start.y.min(end.y)
//
//    fun intersects(other: Line): Boolean {
//        val o1 = orientation(start, end, other.start)
//        val o2 = orientation(start, end, other.end)
//        val o3 = orientation(other.start, other.end, start)
//        val o4 = orientation(other.start, other.end, end)
//
//        if (o1 != o2 && o3 != o4) return true
//
//        if (o1 == 0 && onSegment(start, other.start, end)) return true
//        if (o2 == 0 && onSegment(start, other.end, end)) return true
//        if (o3 == 0 && onSegment(other.start, start, other.end)) return true
//        if (o4 == 0 && onSegment(other.start, end, other.end)) return true
//
//        return false
//    }
//
//    fun onInfiniteLine(point: Point): Boolean {
//        if (end.x == start.x) {
//            return point.x == start.x
//        }
//
//        val slope = (end.y - start.y) / (end.x - start.x)
//        val expectedY = slope * (point.x - start.x) + start.y
//
//        return point.y == expectedY
//    }
//
//    override fun toString(): String = "$start..$end"
//}