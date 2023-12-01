
import utils.Utils.extractNumbers
import java.io.File
import java.util.*

abstract class Solution(val year: Int = Calendar.getInstance().get(Calendar.YEAR) - if (Calendar.getInstance().get(Calendar.MONTH) != Calendar.DECEMBER) 1 else 0) {

    val day: Int = this.javaClass.name.extractNumbers().toInt()

    init {
        require(year in 2015..Calendar.getInstance().get(Calendar.YEAR)) {
            "Invalid year: $year. Must be between 2015 and the current year."
        }

        require(day in 1..25) {
            "Invalid day: $day. Must be between 1 and 25 (inclusive)."
        }
    }

    /**
     * Solves the first part of the puzzle.
     * @param input The puzzle input.
     * @return The solution to the first part.
     */
    abstract fun solvePart1(input: File): Any

    /**
     * Solves the second part of the puzzle.
     * @param input The puzzle input.
     * @return The solution to the second part.
     */
    abstract fun solvePart2(input: File): Any
}
