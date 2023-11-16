# Advent of Code - Kotlin Solutions

Welcome to my Kotlin solutions for [Advent of Code](https://adventofcode.com). This repository is organized to facilitate easy navigation and use of the puzzle solutions.

## Repository Structure

- **Solutions:** Located in the `solutions` directory. Each puzzle solution is a Kotlin class extending the `Solution` base class found in [`src/main/kotlin/Solution.kt`](src/main/kotlin/Solution.kt).
- **Utilities:** Common utilities and generic classes are in the `utils` directory.
- **Test Inputs:** Stored in `src/main/resources/cache/test/$year`. Real puzzle inputs are fetched, cached in `src/main/resources/cache/main/$year`, and follow the format `$year-$day.in`.

### Standard Solution Template

Each solution file should follow the naming convention `Day\d+.kt`. A template for a solution class looks like this:

```kotlin
class Day$day : Solution() {
override fun solvePart1(input: File): Any = TODO()
override fun solvePart2(input: File): Any = TODO()
}
```

The class automatically determines the year from the system calendar and the day from the class name. To specify a different year, pass it in the constructor. Implement `solvePart1()` and `solvePart2()` with your puzzle solutions.

## Running Solutions

Use `gradle run` to execute your solutions. The main executor in `Main.kt` will handle input parsing and execution. Helper methods like `input.rl()` (read trimmed lines) and `input.rt()` (read trimmed text) are available for input processing.

## Creating a New Solution

To add a new solution, simply copy `Day.kt`, rename it to `Day$day.kt`, and implement your solution.
