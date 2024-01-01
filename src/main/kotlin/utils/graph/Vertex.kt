package utils.graph

import utils.point.Point

data class Vertex<T>(val position: Point, val value: T) {
    override fun toString(): String {
        return "($position, $value)"
    }
}
