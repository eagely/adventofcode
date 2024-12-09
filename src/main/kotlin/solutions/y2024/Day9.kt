package solutions.y2024

import Solution
import utils.text
import java.io.File

class Day9 : Solution(2024) {

    data class Space(val pos: Int, val size: Int)
    data class SegFile(val pos: Int, val size: Int, val id: Int)

    override fun solvePart1(input: File): Any {
        val text = input.text.map { it.digitToInt() }
        val e = mutableListOf<Int>()
        var c = 0
        for ((i, t) in text.withIndex()) {
            repeat(t) {
                e.add(if (i % 2 == 0) c else -1)
            }
            if (i % 2 == 0)
                c++
        }
        var p1 = 0
        var p2 = e.size -1

        while (-1 in e) {
            if (e[p1] != -1) {
                p1++
                continue
            }
            e[p1] = e[p2]
            e.removeAt(p2)
            p2--
        }
        return e.withIndex().sumOf { (i, c) -> i.toLong() * c }
    }

    override fun solvePart2(input: File): Any {
        val q = ArrayDeque<SegFile>()
        val space = ArrayDeque<Space>()
        val result = mutableListOf<Int?>()
        var p = 0
        var fileId = 0

        input.text.map { it.digitToInt() }
            .withIndex()
            .forEach { (i, c) ->
                if (i % 2 == 0) {
                    q.add(SegFile(p, c, fileId))
                    repeat((p..<p + c).count()) {
                        result.add(fileId)
                    }
                    p += c
                    fileId += 1
                } else {
                    space.add(Space(p, c))
                    result.addAll(List(c) { null })
                    p += c
                }
            }

        q.reversed().forEach { (pos, size, fid) ->
            space.withIndex().firstOrNull { (_, s) ->
                s.pos < pos && size <= s.size
            }?.let { (si, s) ->
                (0..<size).forEach { i ->
                    result[pos + i] = null
                    result[s.pos + i] = fid
                }
                space[si] = Space(s.pos + size, s.size - size)
            }
        }

        return result.withIndex().sumOf { (i, v) -> i.toLong() * (v ?: 0) }
    }
}