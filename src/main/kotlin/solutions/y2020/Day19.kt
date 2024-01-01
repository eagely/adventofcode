package solutions.y2020

import Solution
import utils.Utils.after
import utils.Utils.before
import utils.Utils.remove
import utils.Utils.sdnl
import utils.Utils.snl
import java.io.File

class Day19 : Solution(2020) {

    private lateinit var rules: HashMap<Int, String>

    override fun solvePart1(input: File) = parse(input).count { it.matches(buildRegex(rules[0]!!).toRegex()) }

    override fun solvePart2(input: File) = parse(input).count { message -> isValid(message, buildRegex(rules[42]!!).toRegex(), buildRegex(rules[31]!!).toRegex()) }

    private fun buildRegex(rule: String): String {
        if (rule == "a" || rule == "b") return rule
        if ("|" in rule) {
            val (a, b) = rule.split(" | ")
            return "(${buildRegex(a)}|${buildRegex(b)})"
        }
        return rule.split(" ").joinToString("") { buildRegex(rules[it.toInt()]!!) }
    }

    private fun isValid(msg: String, regex42: Regex, regex31: Regex): Boolean {
        var rem = msg
        var count42 = 0
        var count31 = 0
        while (regex42.find(rem)?.range?.first == 0) {
            count42++
            rem = rem.removeRange(0, regex42.find(rem)!!.value.length)
        }
        while (regex31.find(rem)?.range?.first == 0) {
            count31++
            rem = rem.removeRange(0, regex31.find(rem)!!.value.length)
        }
        return rem.isEmpty() && count42 > count31 && count31 > 0
    }

    private fun parse(input: File): List<String> {
        val (r, m) = input.sdnl().snl()
        rules = r.associate { it.before(":").toInt() to it.after(": ").remove("\"") }.toMap(hashMapOf())
        return m
    }
}