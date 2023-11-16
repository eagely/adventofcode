
import utils.Utils.extractNumbers
import java.io.File
import java.time.DateTimeException
import java.time.LocalDate
import java.util.*

abstract class Solution(val year: Int = Calendar.getInstance().get(Calendar.YEAR)) {

    val day: Int = this.javaClass.name.extractNumbers().toInt()

    init {
        require(year in 2015..Calendar.getInstance().get(Calendar.YEAR)) {
            "Invalid year: $year. Must be between 2015 and the current year."
        }

        require(day in 1..25) {
            "Invalid day: $day. Must be between 1 and 25 (inclusive)."
        }

        require(!LocalDate.of(year, 12, day).isAfter(LocalDate.now())) {
            throw DateTimeException("Invalid date: December $day, $year. Must not be in the future.")
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
