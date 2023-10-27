package solutions.y2022
import Solution
import java.io.File

class Day2 : Solution(2022) {

    override fun solvePart1(input: File): String {
        val lines: List<String> = input.readLines()
        return playGames(lines).toString()
    }

    override fun solvePart2(input: File): String {
        val lines: List<String> = input.readLines()
        return playTopSecretGames(lines).toString()
    }

    private fun playGames(input: List<String>): Int {
        var result = 0
        for (i in input.indices)
            result += getGameResult(input[i])
        return result
    }

    private fun playTopSecretGames(input: List<String>): Int {
        var result = 0

        for (i in input.indices)
            result += getTopSecretGameResult(input[i])

        return result
    }

    private fun getGameResult(game: String): Int {
        var result = game.last().code - 87
        result += if (game.last().code - game.first().code == 23) 3 else if (game.last().code - game.first().code == 21 || game.last().code - game.first().code == 24) 6 else 0
        return result
    }

    private fun getTopSecretGameResult(game: String): Int {
        var result =
            if (game.last() == 'Y') game.first().code - 64
            else if (game.last() == 'Z') (if (game.first() == 'C') 1 else game.first().code - 63)
            else (if (game.first() == 'A') 3 else game.first().code - 65)
        result += (game.last().code - 88) * 3
        return result
    }
}
