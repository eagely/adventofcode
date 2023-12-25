
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class AdventOfCodeClient(private val sessionCookie: String) {
    private val client = HttpClient {
        install(HttpRedirect) {
            this@HttpClient.followRedirects = true
        }
    }

    private val aoc = "https://adventofcode.com"
    private val userAgent = "Mozilla/5.0"

    suspend fun getPuzzleInput(year: Int, day: Int): String {
        return client.get("$aoc/$year/day/$day/input") {
            headers {
                append("Cookie", "session=$sessionCookie")
                append("User-Agent", userAgent)
            }
        }.bodyAsText()
    }
}
