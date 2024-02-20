package solutions.y2021

import Solution
import utils.*
import java.io.File
import kotlin.math.ceil
import kotlin.math.max

class Day18 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val sf = input.lines.map { Snailfish(it) }
        return sf.drop(1).fold(sf.first()) { acc, snailfish -> acc + snailfish }.number.mag()
    }

    override fun solvePart2(input: File) = input.lines.map { Snailfish(it) }.zipWithAll().maxOf { max((it.first + it.second).number.mag(), (it.second + it.first).number.mag()) }

    data class Snailfish(var number: String) {
        operator fun plus(other: Snailfish): Snailfish {
            var res = "[$number,${other.number}]"
            var old = ""
            while (old != res) {
                old = res
                res = res.boom().split()
            }
            return Snailfish(res)
        }

        private fun String.boom(): String {
            val it = this.iterator().withIndex()
            var nesting = 0
            while (it.hasNext()) {
                val cur = it.next()
                if (cur.value == '[') nesting++
                else if (cur.value == ']') nesting--
                if (nesting == 5) {
                    val explosion = this.ignite(cur.index)
                    return if (explosion != this) explosion.boom() else this
                }
            }
            return this
        }

        private fun String.ignite(index: Int): String {
            var out = this
            return if (this[index] == '[') {
                val end = this.indexOf(']', index)
                val left = this.substring(index + 1, this.indexOf(',', index + 1)).toInt()
                val right = this.substring(this.indexOf(',', index + 1) + 1, end).toInt()
                out = out.substring(0, index) + 'x' + out.substring(end + 1)
                if (this.substring(0, index).extractNumbers().isNotEmpty()) {
                    val range = "\\d+".toRegex().findAll(out.substring(0, index)).last().range
                    out = out.replaceAt(range, (out.substring(range).toInt() + left).toString())
                }
                if (this.substring(end).extractNumbers().isNotEmpty()) {
                    val boomIndex = out.indexOf('x') + 1
                    val range = "\\d+".toRegex().find(out.substring(boomIndex))!!.range
                    out = out.replaceAt(range + boomIndex, (out.substring(range + boomIndex).toInt() + right).toString())
                }
                out.replace('x', '0')
            } else this
        }

        private fun String.split(): String {
            if (this.extractNumbersSeparated().none { it > 9 }) return this
            val range = "\\d{2,}".toRegex().find(this)!!.range
            return this.replaceAt(range, this[range].toInt().let { "[${it / 2},${ceil(it / 2.0).toInt()}]" })
        }
    }

    private fun String.mag(): Long {
        "(\\[\\d+,\\d+])".toRegex().find(this)?.range?.let { return this.replaceAt(it, this.substring(it).extractNumbersSeparated().let { it[0] * 3 + it[1] * 2 }.toString()).mag() }
        return this.toLong()
    }
}