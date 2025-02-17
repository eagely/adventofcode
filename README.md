# Kotlin Solutions - Advent of Code

My solutions for [Advent of Code](https://adventofcode.com) written in Kotlin.
The code primarily uses idiomatic functional programming patterns, though some solutions (particularly from 2022) remain in their original, more novice-oriented style from when I was first learning Kotlin.
Winner of the [Kotlin leaderboard 2024](https://blog.jetbrains.com/kotlin/2025/02/advent-of-code-2024-in-kotlin-winners)! 🏆

## Structure

- **Solutions:** In `solutions` directory, each extending the base `Solution` class
- **Utils:** Helper functions specifically designed for advent of code puzzles in `utils` directory
- **Inputs:** Test inputs in `resources/cache/test/$year`, puzzle inputs cached in `resources/cache/main/$year` (gitignored)

### Template

```kotlin
class Day$day : Solution() {
    override fun solvePart1(input: File): Any = TODO()
    override fun solvePart2(input: File): Any = TODO()
}
