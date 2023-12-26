package solutions.y2020

import Solution
import utils.Utils.sdnl
import java.io.File

class Day4 : Solution(2020) {

    private val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    override fun solvePart1(input: File) = input.sdnl().filter { pass -> fields.all { it in pass } }.size

    override fun solvePart2(input: File) = input.sdnl().filter { pass -> fields.all { it in pass } }.filter {
        it.split(" ", "\n").all {
            val (field, value) = it.split(":")
            when (field) {
                "byr" -> value.toInt() in 1920..2002
                "iyr" -> value.toInt() in 2010..2020
                "eyr" -> value.toInt() in 2020..2030
                "hgt" -> {
                    val (height, unit) = value.partition { it.isDigit() }
                    when (unit) {
                        "cm" -> height.toInt() in 150..193
                        "in" -> height.toInt() in 59..76
                        else -> false
                    }
                }
                "hcl" -> value.matches(Regex("#[0-9a-f]{6}"))
                "ecl" -> value in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                "pid" -> value.matches(Regex("[0-9]{9}"))
                else -> true
            }
        }
    }.size
}