package solutions.y2023

import Solution
import utils.Utils.allEquals
import utils.Utils.lcm
import utils.Utils.rl
import java.io.File

class Day20 : Solution(2023) {

    data class Gate(val name: String, var state: Boolean, val targets: List<String>, val memory: HashMap<String, Boolean> = hashMapOf())

    override fun solvePart1(input: File): Any {
        val gates = input.rl().map {
            val (name, targets) = it.split(" -> ")
            Gate(name, false, targets.split(", "))
        }
        var beep = 0L
        var notbeep = 0L
        val queue = ArrayDeque<Pair<String, Gate>>()
        val bread = gates.first { it.name == "broadcaster" }
        gates.filter { it.name.first() == '&' }.forEach { gate -> gate.memory.putAll(gates.filter { gate.name.drop(1) in it.targets }.associate { it.name to false }) }
        repeat(1000) {
            notbeep++
            queue.addAll(bread.targets.map { it to bread })
            while (queue.isNotEmpty()) {
                val (name, origin) = queue.removeFirst()
                if (origin.state) beep++ else notbeep++
                if (name !in gates.map { it.name.drop(1) }) continue
                val gate = gates.first { it.name.drop(1) == name }
                when (gate.name.first()) {
                    '%' -> {
                        if (origin.state) continue
                        gate.state = !gate.state
                    }

                    '&' -> {
                        gate.memory[origin.name] = origin.state
                        gate.state = !gate.memory.values.allEquals(true)
                    }
                }
                queue.addAll(gate.targets.map { it to gate })
            }
        }
        return notbeep * beep
    }

    override fun solvePart2(input: File): Any {
        val gates = input.rl().map {
            val (name, targets) = it.split(" -> ")
            Gate(name, false, targets.split(", "))
        }
        if (gates.size < 10) return "never (there is no rx)"
        var bonks = 0L
        val queue = ArrayDeque<Pair<String, Gate>>()
        val bread = gates.first { it.name == "broadcaster" }
        gates.filter { it.name.first() == '&' }.forEach { gate -> gate.memory.putAll(gates.filter { gate.name.drop(1) in it.targets }.associate { it.name to false }) }
        val lastconj = gates.first { "rx" in it.targets }.name.drop(1)
        val connectors = gates.filter { it.targets.contains(lastconj) }.map { it.name }
        val cycles = connectors.associate { it.drop(1) to 0L }.toMutableMap()
        while (cycles.any { it.value == 0L }) {
            bonks++
            queue.addAll(bread.targets.map { it to bread })
            while (queue.isNotEmpty()) {
                val (name, origin) = queue.removeFirst()
                if (name == lastconj && origin.state && origin.name in connectors) cycles[origin.name.drop(1)] = bonks
                if (name !in gates.map { it.name.drop(1) }) continue
                val gate = gates.first { it.name.drop(1) == name }
                when (gate.name.first()) {
                    '%' -> {
                        if (origin.state) continue
                        gate.state = !gate.state
                    }

                    '&' -> {
                        gate.memory[origin.name] = origin.state
                        gate.state = !gate.memory.values.allEquals(true)
                    }
                }
                queue.addAll(gate.targets.map { it to gate })
            }
        }
        return lcm(cycles.values)
    }
}