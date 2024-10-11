package solutions.y2018

import Solution
import utils.chargrid
import utils.grid.Grid
import utils.min
import utils.point.Point
import java.io.File

class Day15 : Solution(2018) {
    enum class EntityType {
        ELF, GOBLIN
    }

    data class Entity(var pos: Point, val type: EntityType, var hp: Int = 200)

    override fun solvePart1(input: File): Any {
        var (grid, entitySequence) = parse(input)
        var entities = entitySequence.toMutableList()
        grid = grid.map { if (it in "EG") '.' else it }
        var c = 0

        while (true) {
            val (res, newEntities) = playRound(grid, entities)
            entities = newEntities
            if (!res) return entities.sumOf { it.hp } * c
            c++
        }
    }

    override fun solvePart2(input: File): Any {
        var (grid, entitySequence) = parse(input)
        var entities = entitySequence.toMutableList()
        grid = grid.map { if (it in "EG") '.' else it }
        var c = 0
        val elfCount = entities.count { it.type == EntityType.ELF }
        var cur = 3
        while (true) {
            while (true) {
                val (res, new) = playRound(grid, entities, cur)
                entities = new
                if (entities.count { it.type == EntityType.ELF } < elfCount) break
                if (!res) return entities.sumOf { it.hp } * c
                c++
            }
            cur++
            entities = entitySequence.toMutableList()
            c = 0
        }
    }

    private fun playRound(grid: Grid<Char>, entities: MutableList<Entity>, atk: Int = 3): Pair<Boolean, MutableList<Entity>> {
        for (e in entities.sortedBy { it.pos }) {
            if (e.hp <= 0) continue
            val living = entities.filter { it.hp > 0 }.map { it.pos }.toSet()
            val targets = entities.filter { it.hp > 0 && it.type != e.type }.map { it.pos }.toSet()
            if (targets.isEmpty()) return false to entities.filter { it.hp > 0 }.toMutableList()

            if (e.pos.getCardinalNeighbors().none { it in targets }) {
                val open = targets.flatMap { it.getCardinalNeighbors() }.filter { grid[it] == '.' && it !in living }.toSet()

                val (closest, dist) = getShortestPaths(grid, living, e.pos, open)

                e.pos = e.pos.getCardinalNeighbors().filter { grid[it] != '#' }.sorted().find { n -> getShortestPaths(grid, living, n, open).let { (c, d) -> n !in living && d == dist - 1 && c == closest } } ?: e.pos
            }

            val neighbors = e.pos.getCardinalNeighbors()
            entities.filter { it.pos in neighbors && it.type != e.type && it.hp > 0 }.sortedWith(compareBy<Entity> { it.hp }.thenBy { it.pos }).firstOrNull()?.let { it.hp -= if (e.type == EntityType.ELF) atk else 3 }
        }
        entities.removeIf { it.hp < 0 }
        return true to entities
    }

    private fun getShortestPaths(grid: Grid<Char>, exclude: Set<Point>, start: Point, goals: Set<Point>): Pair<Point, Int> {
        val q = ArrayDeque<Pair<Point, Int>>()
        val seen = HashSet<Point>()
        var minDist: Int? = null
        var minPoint: Point? = null
        q.add(start to 0)

        while (q.isNotEmpty()) {
            val (cur, dist) = q.removeFirst()
            if (!seen.add(cur)) continue
            if (minDist != null && dist > minDist) return (minPoint ?: break) to minDist
            if (cur in goals) {
                minDist = dist
                minPoint = minPoint?.let { min(cur, it) } ?: cur
            }
            q.addAll(cur.getCardinalNeighbors().filter { p -> grid[p] == '.' && p !in exclude }.map { it to dist + 1 })
        }

        if (minPoint != null && minDist != null) return minPoint to minDist
        return start to 0
    }

    private fun parse(input: File): Pair<Grid<Char>, Sequence<Entity>> {
        val grid = input.chargrid()
        return grid to grid.data.asSequence().filter { (_, v) -> v in "EG" }.map { (p, v) -> Entity(p, if (v == 'E') EntityType.ELF else EntityType.GOBLIN) }.sortedBy { it.pos }
    }
}