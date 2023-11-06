
import kotlinx.coroutines.runBlocking
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.time.DateTimeException
import java.time.LocalDate
import javax.swing.JOptionPane

fun main() = runBlocking {
    val client =
        AdventOfCodeClient("53616c7465645f5f093a85acbdc5d530501f4739834f19022cc9c59b326d4e14955b0a30684295f6ed09537d646de2db501563092e92ba38cb752ab5d5e3f867")
    val solutions = getSolutions()

    solutions.forEach { instance ->
        val cacheDir = "src/main/resources/cache/main/${instance.year}"
        val testDir = "src/main/resources/cache/test/${instance.year}"
        val cachedInput = File(cacheDir, "${instance.year}-${instance.day}.in")
        val testInput = File(testDir, "${instance.year}-${instance.day}.in")
        val part1Output = File(cacheDir, "output_part1.txt")
        val part2Output = File(cacheDir, "output_part2.txt")

        val input: File = if (cachedInput.exists()) {
            cachedInput
        } else {
            if(LocalDate.of(instance.year, 12, instance.day).isAfter(LocalDate.now()))
                throw DateTimeException("Requested date is in the future, puzzle not yet unlocked.")
            val fetchedInput = client.getPuzzleInput(instance.year, instance.day)
            File(cacheDir).mkdirs()
            File(testDir).mkdirs()
            testInput.createNewFile()
            cachedInput.writeText(fetchedInput)
            cachedInput
        }

        if (!part1Output.exists()) {
            var start = System.currentTimeMillis()
            val testOutput = if(testInput.exists() && testInput.readLines().isNotEmpty()) instance.solvePart1(testInput).toString() else ""
            val time = System.currentTimeMillis() - start
            if(testInput.exists() && testInput.readLines().isNotEmpty()) println("Test Part 1: $testOutput ($time ms)")
            start = System.currentTimeMillis()
            val output = instance.solvePart1(input).toString()

            println("Real Part 1: $output (${System.currentTimeMillis() - start} ms)")
            if(output != "0" && output != "")
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(output), null)


//            if (showPopup("Part 1 solution: $output. Submit?")) {
//                val result = client.submitSolution(instance.year, instance.day, 1, output)
//                JOptionPane.showMessageDialog(null, result)
//                if (result.contains("right"))
//                    part1Output.writeText(output)
//            }
        }

        if (!part2Output.exists()) {
            var start = System.currentTimeMillis()
            val testOutput = if(testInput.exists() && testInput.readLines().isNotEmpty()) instance.solvePart2(testInput).toString() else ""
            val time = System.currentTimeMillis() - start
            if(testInput.exists() && testInput.readLines().isNotEmpty()) println("Test Part 2: $testOutput ($time ms)")
            start = System.currentTimeMillis()
            val output = instance.solvePart2(input).toString()
            println("Real Part 2: $output (${System.currentTimeMillis() - start} ms)")
            if(output != "0" && output != "")
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(output), null)
//            if (showPopup("Part 2 solution: $output. Submit?")) {
//                val result = client.submitAnswer(instance.year, instance.day, 2, output)
//                println(result)
////                JOptionPane.showMessageDialog(null, result)
//                if (result.contains("right"))
//                    outputFile.writeText(output)  // Cache the submitted answer
//            }
        }
    }
}

fun showPopup(message: String): Boolean {
    val options = arrayOf("Yes", "No")
    val response = JOptionPane.showOptionDialog(
        null,
        message,
        "Submit Answer?",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    )
    return response == JOptionPane.YES_OPTION
}

fun getSolutions(): List<Solution> {
    val solutionsDir = File("src/main/kotlin/solutions")
    val currentFiles = solutionsDir.listFiles()?.map { it.name } ?: emptyList()

    return listOf(currentFiles
        .filter { it.endsWith(".kt") && it.startsWith("Day") && it != "Day.kt" }  // Filter out the template "Day.kt"
        .map { it.removeSuffix(".kt") }
        .map {
            Class.forName("solutions.$it").getDeclaredConstructor().newInstance() as Solution
        }.sortedBy { it.day }.last())
}
