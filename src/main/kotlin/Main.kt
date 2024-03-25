import kotlinx.coroutines.runBlocking
import utils.annotations.NoReal
import utils.annotations.NoTest
import utils.copyToClipboard
import utils.rt
import java.io.File
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.system.measureTimeMillis

@Suppress("deprecated")
fun main() = runBlocking {
    val client = AdventOfCodeClient(System.getenv("AOC_COOKIE"))
    val solutions = getSolution()

    solutions.forEach { instance ->
        println("Running ${instance.day}")
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
            val solveMethod = if (part == 1) instance::solvePart1 else instance::solvePart2
            val methodName = if (part == 1) "solvePart1" else "solvePart2"
            val check = instance::class.memberFunctions.firstOrNull { it.name == methodName }
            val isNoTestAnnotated = check?.findAnnotation<NoTest>() != null
            val isNoRealAnnotated = check?.findAnnotation<NoReal>() != null

            var test: String
            var real: String
            if (!isNoTestAnnotated && tin.exists() && tin.rt().isNotEmpty()) {
                val tt = measureTimeMillis {
                    test = solveMethod(tin).toString()
                }
                println("Test Part $part: $test ($tt ms)")
            }
            if (!isNoRealAnnotated && rin.exists() && rin.rt().isNotEmpty()) {
                val tr = measureTimeMillis {
                    real = solveMethod(rin).toString()
                }
                println("Real Part $part: $real ($tr ms)")
                if (real != "0" && real != "") copyToClipboard(real)
            }
        }

        run(1)
        run(2)
    }
}

fun getSolution(): List<Solution> {
    val solutionsDir = File("src/main/kotlin/solutions")
    val currentFiles = solutionsDir.listFiles()?.map { it.name } ?: emptyList()

    solutionsDir.listFiles()?.forEach { file ->
        if (file.isFile && file.extension == "kt") {
            val contents = file.readText()
            if (".readText()" in contents) {
                println("Warning: Usage of readText() found in ${file.path}, this function is dangerous as inputs usually have trailing newlines and these could be interpreted by your program to be part of the input. Use File.text or File.rt() helper methods from Helper.kt instead")
            }
            if (".readLines()" in contents) {
                println("Warning: Usage of readLines() found in ${file.path}, this function is dangerous as inputs usually have trailing newlines and these could be interpreted by your program to be part of the input. Use File.lines or File.rl() helper methods from Helper.kt instead")
            }
        }
    }

    return currentFiles.asSequence().filter { it.matches(Regex("Day\\d+\\.kt")) && it != "Day.kt" }.map { Class.forName("solutions.${it.removeSuffix(".kt")}").getDeclaredConstructor().newInstance() as Solution }.toList()
}