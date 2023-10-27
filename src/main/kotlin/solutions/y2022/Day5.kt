package solutions.y2022
import Solution
import java.io.File

import java.util.*

class Day5 : Solution(2022) {


    override fun solvePart1(input: File): String {
        val lines = input.readLines()
        return getTopItemsUsingCrateMover9000(lines)
    }

    override fun solvePart2(input: File): String {
        val lines = input.readLines()
        return getTopItemsUsingCrateMover9001(lines)
    }

    private fun getStackSize(input: List<String>): Int {
        var stackSize = -1
        while (input[++stackSize] != "");
        return stackSize - 1
    }


    private fun parseInput(input: List<String>): ArrayList<Stack<Char>> {
        val stack = ArrayList<Stack<Char>>()
        val stackSize = getStackSize(input)
        val amountOfStacks = input[stackSize].trim().last().digitToInt()

        for (i in 0 ..< amountOfStacks)
            stack.add(Stack<Char>())
        for (i in 0 ..< stackSize)
            for (j in 0 ..< amountOfStacks)
                if (input[i][j * 4 + 1] != ' ')
                    stack[j].push(input[i][j * 4 + 1])

        for (i in stack.indices)
            stack[i].reverse()
        return stack
    }

    private fun getTopItemsUsingCrateMover9000(input: List<String>): String {
        val stack = parseInput(input)
        val stackSize = getStackSize(input)
        var move: Int
        var from: Int
        var to: Int
        var topItems = ""
        for (i in stackSize + 2 ..< input.size) {
            move = Integer.parseInt(input[i].substring(input[i].indexOf("move ") + 5, input[i].indexOf(" from ")))
            from = Integer.parseInt(input[i].substring(input[i].indexOf(" from ") + 6, input[i].indexOf(" to "))) - 1
            to = Integer.parseInt(input[i].substringAfter(" to ")) - 1
            for (j in 0 ..< move) {
                stack[to].push(stack[from].peek())
                stack[from].pop()
            }
        }
        for(i in stack.indices)
            topItems += stack[i].peek()
        return topItems
    }
    private fun getTopItemsUsingCrateMover9001(input: List<String>): String {
        val stack = parseInput(input)
        val stackSize = getStackSize(input)
        val temporaryDeposit = Stack<Char>()
        var move: Int
        var from: Int
        var to: Int
        var topItems = ""
        for (i in stackSize + 2 ..< input.size) {
            move = Integer.parseInt(input[i].substring(input[i].indexOf("move ") + 5, input[i].indexOf(" from ")))
            from = Integer.parseInt(input[i].substring(input[i].indexOf("from ") + 5, input[i].indexOf(" to "))) - 1
            to = Integer.parseInt(input[i].substringAfter(" to ")) - 1
            for(j in 0 ..< move) {
                temporaryDeposit.push(stack[from].peek())
                stack[from].pop()
            }
            for(j in 0 ..< move) {
                stack[to].push(temporaryDeposit.peek())
                temporaryDeposit.pop()
            }
        }
        for(i in stack.indices)
            topItems += stack[i].peek()
        return topItems
    }
}
