import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class AdventOfCodeClient(private val sessionCookie: String) {
    private val client = HttpClient {
        install(HttpRedirect) {
            followRedirects = true
        }
    }

    private val aoc = "https://adventofcode.com"
    private val userAgent = "Mozilla/5.0"

    suspend fun getPuzzleInput(year: Int, day: Int): String {
        return client.get<String>("$aoc/$year/day/$day/input") {
            headers {
                append("Cookie", "session=$sessionCookie")
                append("User-Agent", userAgent)
            }
        }
    }
}
