package solutions.y2020

import Solution
import utils.Utils.extractNumbers
import utils.Utils.isqrt
import utils.Utils.join
import utils.Utils.product
import utils.Utils.sdanl
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day20 : Solution(2020) {

    override fun solvePart1(input: File) = parse(input).filter { it sharing parse(input) == 2 }.map { it.id }.product()

    override fun solvePart2(input: File): Any {
        val photos = parse(input)
        val width = isqrt(photos.size)
        var p = photos.first { it sharing photos == 2 }.variations().first { it.isShared(Direction.SOUTH, photos) && it.isShared(Direction.EAST, photos) }
        var hdr = p
        return Photo(0, (0..<width).map { r -> (0..<width).map { c ->
            when {
                r == 0 && c == 0 -> p
                c == 0 -> {
                    hdr = hdr.connect(Direction.SOUTH, Direction.NORTH, photos)
                    p = hdr
                    hdr
                }
                else -> {
                    p = p.connect(Direction.EAST, Direction.WEST, photos)
                    p
                }
            }
        }
        }.flatMap { row -> (1..<photos.first().photo.size - 1).map { y -> row.joinToString("") { it.photo[y].drop(1).dropLast(1).join() }.toCharArray() } }).variations().first { it has setOf(Point(0, 18), Point(1, 0), Point(1, 5), Point(1, 6), Point(1, 11), Point(1, 12), Point(1, 17), Point(1, 18), Point(1, 19), Point(2, 1), Point(2, 4), Point(2, 7), Point(2, 10), Point(2, 13), Point(2, 16)) }.photo.sumOf { it.count { it == '#' } }
    }

    private fun parse(input: File): List<Photo> = input.sdanl().map { Photo(it.first().extractNumbers().toLong(), it.drop(1).map { it.toCharArray() }) }

    private class Photo(val id: Long, var photo: List<CharArray>) {

        private val sides = Direction.entries.map { facing(it) }.toSet()

        fun connect(my: Direction, other: Direction, photos: List<Photo>) = photos.filterNot { it.id == id }.first { it contains facing(my) }.also { it.orient(facing(my), other) }

        fun variations() = sequence {
            repeat(2) {
                repeat(4) {
                    yield(this@Photo.rotate())
                }
                this@Photo.flip()
            }
        }

        fun isShared(dir: Direction, photos: List<Photo>) = photos.filterNot { it.id == id }.any { it contains facing(dir) }

        infix fun sharing(photos: List<Photo>) = sides.sumOf { side -> photos.filterNot { it.id == id }.count { it contains side } }

        infix fun has(mask: Set<Point>): Boolean {
            var found = false
            (0..(photo.size - mask.maxByOrNull { it.x }!!.x)).forEach { x ->
                (0..(photo.size - mask.maxByOrNull { it.y }!!.y)).forEach { y ->
                    val mloc = mask.map { it + Point(x, y) }
                    if (mloc.all { photo[it.x][it.y] == '#' }) {
                        found = true
                        mloc.forEach { photo[it.x][it.y] = '.' }
                    }
                }
            }
            return found
        }

        private fun rotate(): Photo {
            photo = photo.mapIndexed { x, row -> row.mapIndexed { y, _ -> photo[y][x] }.reversed().toCharArray() }
            return this
        }

        private fun flip(): Photo {
            photo = photo.map { it.reversed().toCharArray() }
            return this
        }

        private fun facing(dir: Direction) = when (dir) {
            Direction.NORTH -> photo.first().join()
            Direction.SOUTH -> photo.last().join()
            Direction.WEST -> photo.map { it.first() }.join()
            Direction.EAST -> photo.map { it.last() }.join()
        }

        private fun orient(side: String, dir: Direction) = variations().first { it.facing(dir) == side }

        private infix fun contains(side: String) = side in sides || side in sides.map { it.reversed() }
    }
}