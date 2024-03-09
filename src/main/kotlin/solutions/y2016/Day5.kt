package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day5 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val id = input.text
        var out = ""
        var i = 0
        repeat(8) {
            while (true) {
                val hash = "$id${i++}".md5()
                if (hash.startsWith("00000")) {
                    out += hash[5]
                    break
                }
            }
        }
        return out
    }

    override fun solvePart2(input: File): Any {
        val id = input.text
        val out = CharArray(8) { '_' }
        var i = 0
        repeat(8) {
            while (true) {
                val hash = "$id${i++}".md5()
                if (hash.startsWith("00000")) {
                    val idx = hash[5].toString().toInt(16)
                    if (idx in 0..7 && out[idx] == '_') {
                        out[idx] = hash[6]
                        break
                    }
                }
            }
        }
        return out.join()
    }
}