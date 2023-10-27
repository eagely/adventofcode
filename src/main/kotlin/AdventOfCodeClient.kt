import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import java.net.HttpURLConnection
import java.net.URL

class AdventOfCodeClient(private val sessionCookie: String) {

    private val client = HttpClient {
        install(HttpRedirect) {
            followRedirects = true
        }
    }

    private val aoc = "https://adventofcode.com"
    private val userAgent = "eagely/1.0"

    suspend fun getPuzzleInput(year: Int, day: Int): String {
        return client.get<String>("$aoc/$year/day/$day/input") {
            headers {
                append("Cookie", "session=$sessionCookie")
                append("User-Agent", userAgent)
            }
        }
    }

    fun submitSolution(year: Int, day: Int, part: Int, answer: String): String {
        val url = URL("$aoc/$year/day/$day/answer")
        val params = "level=$part&answer=$answer"

        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("User-Agent", userAgent)
        conn.setRequestProperty("Cookie", "session=$sessionCookie")
        conn.doOutput = true

        conn.outputStream.writer().apply {
            write(params)
            flush()
        }

        return conn.inputStream.reader().readText()
    }
}
