package utils

class IntCode(private val program: IntArray, private val input: Int) {
    private var pointer = 0
    private val output = mutableListOf<Int>()

    fun run(): List<Int> {
        while (true) {
            val opcode = program[pointer] % 100
            if (opcode == 99) break

            val modes = listOf(
                program[pointer] / 100 % 10,
                program[pointer] / 1000 % 10,
                program[pointer] / 10000 % 10
            )

            fun getParam(index: Int) = if (modes[index - 1] == 0) program[program[pointer + index]] else program[pointer + index]

            when (opcode) {
                1 -> {
                    val dest = program[pointer + 3]
                    program[dest] = getParam(1) + getParam(2)
                    pointer += 4
                }
                2 -> {
                    val dest = program[pointer + 3]
                    program[dest] = getParam(1) * getParam(2)
                    pointer += 4
                }
                3 -> {
                    val dest = program[pointer + 1]
                    program[dest] = input
                    pointer += 2
                }
                4 -> {
                    output.add(getParam(1))
                    pointer += 2
                }
                5 -> {
                    if (getParam(1) != 0) {
                        pointer = getParam(2)
                    } else {
                        pointer += 3
                    }
                }
                6 -> {
                    if (getParam(1) == 0) {
                        pointer = getParam(2)
                    } else {
                        pointer += 3
                    }
                }
                7 -> {
                    val dest = program[pointer + 3]
                    program[dest] = if (getParam(1) < getParam(2)) 1 else 0
                    pointer += 4
                }
                8 -> {
                    val dest = program[pointer + 3]
                    program[dest] = if (getParam(1) == getParam(2)) 1 else 0
                    pointer += 4
                }
            }
        }
        return output
    }
}