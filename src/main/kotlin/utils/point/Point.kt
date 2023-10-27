package utils.point

data class Point(var x: Int, var y: Int) {
    constructor(x: Number, y: Number) : this(x.toInt(), y.toInt())

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun unaryPlus() = Point(+x, +y)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun unaryMinus() = Point(-x, -y)

    operator fun times(other: Point) = Point(x * other.x, y * other.y)

    operator fun div(other: Point) = Point(x / other.x, y / other.y)

    operator fun rem(other: Point) = Point(x % other.x, y % other.y)

    operator fun inc() = Point(x + 1, y + 1)

    operator fun dec() = Point(x - 1, y - 1)

    operator fun compareTo(other: Point) = (x + y).compareTo(other.x + other.y)

    override fun toString() = "$x-$y"
}