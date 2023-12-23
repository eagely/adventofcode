package solutions.y2023

import Solution
import utils.Utils.rl
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day23 : Solution(2023) {
    
    private lateinit var grid: Array<Array<Char>>
    private val visited = mutableSetOf<Point>()
    private var max = 0

    override fun solvePart1(input: File): Any {
        grid = input.rl().map { it.toCharArray().toTypedArray() }.toTypedArray()
        dfs(Point(0, grid[0].indexOf('.')), Point(grid.size - 1, grid.last().lastIndexOf('.')))
        return max
    }

    override fun solvePart2(input: File): Any {
        grid = input.rl().map { it.toCharArray().toTypedArray() }.toTypedArray()
        val start = Point(0, grid[0].indexOf('.'))
        val end = Point(grid.size - 1, grid.last().indexOf('.'))
        return dfsOptimized(makeAdjacencies(grid), start, end, mutableMapOf())!!
    }

    private fun dfs(cur: Point, end: Point, steps: Int = 0) {
        if (cur == end) {
            max = maxOf(max, steps)
            return
        }
        visited.add(cur)
        getNextPoints(cur).filter { it !in visited }.forEach { dfs(it, end, steps + 1) }
        visited.remove(cur)
    }

    private fun makeAdjacencies(grid: Array<Array<Char>>): Map<Point, Map<Point, Int>> {
        val adjacencies = grid.indices.flatMap { x ->
            grid[0].indices.mapNotNull { y ->
                if (grid[x][y] != '#') {
                    Point(x, y) to Direction.entries.mapNotNull { (dx, dy) ->
                        val nx = x + dx
                        val ny = y + dy
                        if (nx in grid.indices && ny in grid[0].indices && grid[nx][ny] != '#') Point(nx, ny) to 1 else null
                    }.toMap(mutableMapOf())
                } else null
            }
        }.toMap(mutableMapOf())

        adjacencies.keys.toList().forEach { key ->
            adjacencies[key]?.takeIf { it.size == 2 }?.let { neighbors ->
                val left = neighbors.keys.first()
                val right = neighbors.keys.last()
                val totalSteps = neighbors[left]!! + neighbors[right]!!
                adjacencies.getOrPut(left) { mutableMapOf() }.merge(right, totalSteps, ::maxOf)
                adjacencies.getOrPut(right) { mutableMapOf() }.merge(left, totalSteps, ::maxOf)
                listOf(left, right).forEach { adjacencies[it]?.remove(key) }
                adjacencies.remove(key)
            }
        }
        return adjacencies
    }

    private fun dfsOptimized(graph: Map<Point, Map<Point, Int>>, cur: Point, end: Point, seen: MutableMap<Point, Int>): Int? {
        if (cur == end) {
            return seen.values.sum()
        }

        var best: Int? = null
        (graph[cur] ?: emptyMap()).forEach { (neighbor, steps) ->
            if (neighbor !in seen) {
                seen[neighbor] = steps
                val res = dfsOptimized(graph, neighbor, end, seen)
                if (best == null || (res != null && res > best!!)) {
                    best = res
                }
                seen.remove(neighbor)
            }
        }
        return best
    }

    private fun getNextPoints(point: Point): List<Point> {
        val cur = grid[point.x][point.y]
        return Direction.entries.mapNotNull { dir ->
            val p = point + dir
            if (p.x in grid.indices && p.y in grid[0].indices && grid[p.x][p.y] != '#' && (cur == '.' || Direction.of(cur) == dir)) p else null
        }
    }
}