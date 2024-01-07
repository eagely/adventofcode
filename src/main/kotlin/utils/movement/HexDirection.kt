package utils.movement

import utils.point.Point

enum class HexDirection(val point: Point) {
    NW(Point(-1, -1)),
    NE(Point(0, -1)),
    E(Point(1, 0)),
    SE(Point(1, 1)),
    SW(Point(0, 1)),
    W(Point(-1, 0));


    companion object {
        fun getNeighbors(point: Point) = listOf(NW.point, NE.point, E.point, SE.point, SW.point, W.point).map { point + it }
        fun of(string: String) = when (string) {
            "nw" -> NW
            "ne" -> NE
            "e" -> E
            "se" -> SE
            "sw" -> SW
            "w" -> W
            else -> throw IllegalArgumentException("Invalid direction: $string")
        }
    }
}