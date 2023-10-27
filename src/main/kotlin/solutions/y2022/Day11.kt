package solutions.y2022
import Solution
import java.io.File

class Day11 : Solution(2022) {


    override fun solvePart1(input: File) = getWorryWithRelief(input.readLines()).toString()

    override fun solvePart2(input: File) = getWorry(input.readLines()).toString()

    private fun getAmountOfMonkeys(input: List<String>): Int {
        var amount = 0

        for (i in input.indices)
            if (input[i].startsWith("Monkey "))
                amount++

        return amount
    }

    private fun getFalseResult(input: List<String>): ArrayList<Int> {
        val falseResult = ArrayList<Int>()

        for (i in input.indices) {
            if (input[i].startsWith("    If false: throw to monkey "))
                falseResult.add(input[i].substringAfter("    If false: throw to monkey ").toInt())
        }

        return falseResult
    }

    private fun getTrueResult(input: List<String>): ArrayList<Int> {
        val trueResult = ArrayList<Int>()

        for (i in input.indices) {
            if (input[i].startsWith("    If true: throw to monkey "))
                trueResult.add(input[i].substringAfter("    If true: throw to monkey ").toInt())
        }

        return trueResult
    }

    private fun getOperation(input: List<String>): ArrayList<String> {
        val operation = ArrayList<String>()

        for (i in input.indices)
            if (input[i].startsWith("  Operation: new = old "))
                operation.add(input[i].substringAfter("  Operation: new = old "))

        return operation
    }

    private fun getTest(input: List<String>): ArrayList<Long> {
        val test = ArrayList<Long>()

        for (i in input.indices)
            if (input[i].startsWith("  Test: divisible by "))
                test.add(input[i].substringAfter("  Test: divisible by ").toLong())

        return test
    }

    private fun getStartingItems(input: List<String>): ArrayList<ArrayList<Long>> {
        val output = ArrayList<ArrayList<Long>>()

        for (i in 0 ..< getAmountOfMonkeys(input))
            output.add(ArrayList())

        var itemList: String
        var monkey = 0

        for (i in input.indices) {
            if (input[i].startsWith("  Starting items: ")) {
                itemList = input[i].substringAfter("  Starting items: ")
                while (itemList.contains(",")) {
                    output[monkey].add(itemList.substringBefore(",").toLong())
                    itemList = itemList.substringAfter(" ")
                }
                output[monkey++].add(itemList.toLong())
            }
        }

        return output
    }

    private fun evalEquivalent(old: Long, operation: String): Long {
        return if (operation.startsWith("*")) {
            if (operation.endsWith("old"))
                old * old
            else
                old * operation.substringAfter(" ").toInt()
        } else
            old + operation.substringAfter(" ").toInt()
    }

    private fun getWorryWithRelief(input: List<String>): Long {
        val operation = getOperation(input)
        val test = getTest(input)
        val trueResult = getTrueResult(input)
        val falseResult = getFalseResult(input)

        val worryLevel = getStartingItems(input)
        val monkeyActivity = ArrayList<Int>()

        for (i in 0 ..< getAmountOfMonkeys(input))
            monkeyActivity.add(0)

        for (round in 0 ..< 20)
            for (monkey in 0 ..< getAmountOfMonkeys(input))
                while (worryLevel[monkey].isNotEmpty()) {

                    worryLevel[monkey][0] = evalEquivalent(worryLevel[monkey][0], operation[monkey])

                    worryLevel[monkey][0] = worryLevel[monkey][0] / 3

                    if (worryLevel[monkey][0] % test[monkey] == 0L)
                        worryLevel[trueResult[monkey]].add(worryLevel[monkey][0])
                    else
                        worryLevel[falseResult[monkey]].add(worryLevel[monkey][0])

                    worryLevel[monkey].removeAt(0)
                    monkeyActivity[monkey]++
                }
        return getMonkeyBusiness(monkeyActivity)
    }

    private fun getWorry(input: List<String>): Long {
        val operation = getOperation(input)
        val test = getTest(input)
        val trueResult = getTrueResult(input)
        val falseResult = getFalseResult(input)
        val modulo = getModulo(test)

        val worryLevel: ArrayList<ArrayList<Long>> = getStartingItems(input)
        val monkeyActivity = ArrayList<Int>()

        for (i in 0 ..< getAmountOfMonkeys(input))
            monkeyActivity.add(0)

        for (round in 0 ..< 10000)
            for (monkey in 0..<getAmountOfMonkeys(input))
                while (worryLevel[monkey].isNotEmpty()) {
                    worryLevel[monkey][0] = evalEquivalent(worryLevel[monkey][0], operation[monkey])
                    if (worryLevel[monkey][0] % test[monkey] == 0L)
                        worryLevel[trueResult[monkey]].add(worryLevel[monkey][0] % modulo)
                    else
                        worryLevel[falseResult[monkey]].add(worryLevel[monkey][0] % modulo)

                    worryLevel[monkey].removeAt(0)
                    monkeyActivity[monkey]++
                }
        return getMonkeyBusiness(monkeyActivity)
    }

    private fun getMonkeyBusiness(monkeyActivity: ArrayList<Int>): Long {
        var output = 0
        var secondHighestActivity = 0

        for (i in monkeyActivity.indices) {
            if (monkeyActivity[i] > output) {
                secondHighestActivity = output
                output = monkeyActivity[i]
            } else if (monkeyActivity[i] > secondHighestActivity)
                secondHighestActivity = monkeyActivity[i]
        }

        return output.toLong() * secondHighestActivity.toLong()
    }

    private fun getModulo(test: ArrayList<Long>): Long {
        var modulo: Long = 1
        for (i in test.indices)
            modulo *= test[i]
        return modulo
    }
}
