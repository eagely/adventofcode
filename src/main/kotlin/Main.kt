
import kotlinx.coroutines.runBlocking
import utils.Utils.copyToClipboard
import utils.Utils.rt
import java.io.File
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val client = AdventOfCodeClient("53616c7465645f5f13ea761af63a778b2539ff4e5d636a65a3062192cfa51457eeda4dae2c0445dd799541c4d4a572e71d5ad2f70ebb0b3b8ae7fd74cd5ff305")
    val instance = getSolution()

    val rdir = "src/main/resources/cache/main/${instance.year}"
    val tdir = "src/main/resources/cache/test/${instance.year}"
    val rin = File(rdir, "${instance.year}-${instance.day}.in")
    val tin = File(tdir, "${instance.year}-${instance.day}.in")

    val input: File = if (rin.exists()) rin
    else {
        File(rdir).mkdirs()
        File(tdir).mkdirs()
        tin.createNewFile()
        rin.writeText(client.getPuzzleInput(instance.year, instance.day))
        rin
    }

    fun run(part: Int) {
        val solve = if(part == 1) instance::solvePart1 else instance::solvePart2
        var test: String
        var real: String
        if (tin.rt().isNotEmpty()) {
            val tt = measureTimeMillis {
                test = if (tin.exists() && tin.readLines().isNotEmpty()) solve(tin).toString() else ""
            }
            println("Test Part $part: $test ($tt ms)")
        }
        val time = measureTimeMillis { real = solve(input).toString() }
        println("Real Part $part: $real ($time ms)")
        if (real != "0" && real != "") copyToClipboard(real)
    }

    run(1)
    run(2)
}

fun getSolution(): Solution {
    val solutionsDir = File("src/main/kotlin/solutions")
    val currentFiles = solutionsDir.listFiles()?.map { it.name } ?: emptyList()

    return currentFiles
        .asSequence()
        .filter { it.matches(Regex("Day\\d+\\.kt")) && it != "Day.kt" }
        .map { Class.forName("solutions.${it.removeSuffix(".kt")}").getDeclaredConstructor().newInstance() as Solution }
        .maxByOrNull { it.day }
        ?: throw IllegalStateException("No solution files found.")
}