package utils.point

import java.math.BigDecimal

data class Point(var x: BigDecimal, var y: BigDecimal) {
    constructor(x: Number, y: Number) : this(BigDecimal(x.toString()), BigDecimal(y.toString()))

    operator fun plus(other: Point) = Point(x.add(other.x), y.add(other.y))

    operator fun unaryPlus() = Point(x.plus(), y.plus())

    operator fun minus(other: Point) = Point(x.subtract(other.x), y.subtract(other.y))

    operator fun unaryMinus() = Point(x.negate(), y.negate())

    operator fun times(other: Point) = Point(x.multiply(other.x), y.multiply(other.y))

    operator fun div(other: Point) = Point(x.divide(other.x), y.divide(other.y))

    operator fun rem(other: Point) = Point(x.remainder(other.x), y.remainder(other.y))

    operator fun inc() = Point(x.add(BigDecimal.ONE), y.add(BigDecimal.ONE))

    operator fun dec() = Point(x.subtract(BigDecimal.ONE), y.subtract(BigDecimal.ONE))

    operator fun compareTo(other: Point) = (x.add(y)).compareTo(other.x.add(other.y))

    override fun toString() = "$x-$y"
}