package utils.point

import java.math.BigDecimal

data class Point3D(var x: BigDecimal, var y: BigDecimal, var z: BigDecimal) {
    constructor(x: Number, y: Number, z: Number) : this(BigDecimal(x.toString()), BigDecimal(y.toString()), BigDecimal(z.toString()))
    operator fun plus(point: Point3D) = Point3D(x + point.x, y + point.y, z + point.z)

    operator fun unaryPlus() = this.copy()

    operator fun minus(point: Point3D) = Point3D(x - point.x, y - point.y, z - point.z)

    operator fun unaryMinus() = Point3D(-x, -y, -z)

    operator fun times(point: Point3D) = Point3D(x * point.x, y * point.y, z * point.z)

    operator fun div(point: Point3D) = Point3D(x / point.x, y / point.y, z / point.z)

    operator fun rem(point: Point3D) = Point3D(x % point.x, y % point.y, z % point.z)

    operator fun inc() = Point3D(x + BigDecimal.ONE, y + BigDecimal.ONE, z + BigDecimal.ONE)

    operator fun dec() = Point3D(x - BigDecimal.ONE, y - BigDecimal.ONE, z - BigDecimal.ONE)

    fun isSimilar(point: Point3D) = x == point.x || y == point.y || z == point.z

    override fun toString() = "$x-$y-$z"
}