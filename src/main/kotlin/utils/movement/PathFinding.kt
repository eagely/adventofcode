package utils.movement

import java.util.*

/**
 * Generic path finding utils.
 * Mainly designed for grids.
 * Currently O(n) for bfs and O(n² log n) for dijkstra.
 */
object PathFinding {
    data class Weighted<out State>(val state: State, val weight: Int)

    data class BfsResult<out State>(val path: List<State>) {
        val start get() = if (path.isNotEmpty()) path.first() else null
        val end get() = if (path.isNotEmpty()) path.last() else null
        val length get() = path.size - 1

        override fun toString(): String {
            return "BfsResult(start=$start, end=$end, length=$length)"
        }
    }

    data class DijkstraResult<out State>(val path: List<State>, val cost: Int) {
        constructor(weightedPath: List<Weighted<State>>) : this(weightedPath.map { it.state }, weightedPath.sumOf { it.weight })
        val start get() = if (path.isNotEmpty()) path.first() else null
        val end get() = if (path.isNotEmpty()) path.last() else null
        val length get() = path.size - 1

        override fun toString(): String {
            return "DijkstraResult(start=$start, end=$end, length=$length, cost=$cost)"
        }
    }

    data class SingleSourceBacktrack<State>(val map: Map<State, State>) {
        fun getPath(end: State): BfsResult<State> {
            val path = mutableListOf<State>()
            var current: State? = end
            while (current != null) {
                path.add(current)
                current = map[current]
            }
            if (path.size == 1) return BfsResult(listOf())
            return BfsResult(path.reversed())
        }

        fun getPath(isEnd: (State) -> Boolean) = map.keys.filter { isEnd(it) }.map { getPath(it) }.sortedBy { it.length }.first { it.length != -1 }
    }

    data class SingleSourceWeightedBacktrack<State>(val map: Map<Weighted<State>, Weighted<State>>) {
        fun getPath(end: State): DijkstraResult<State> {
            val path = mutableListOf<Weighted<State>>()
            var current: Weighted<State>? = map.filterKeys { it.state == end }.keys.firstOrNull()
            while (current != null) {
                path.add(current)
                current = map[current]!!
            }
            if (path.size == 1) return DijkstraResult(listOf(), 0)
            return DijkstraResult(path.reversed().map { it.state }, path.sumOf { it.weight })
        }

        fun getPath(isEnd: (State) -> Boolean) = map.keys.filter { isEnd(it.state) }.map { getPath(it.state) }.sortedBy { it.cost }.first { it.cost != -1 }
    }

    inline fun <State> bfsAllPaths(start: State, next: (State) -> Iterable<State>): SingleSourceBacktrack<State> {
        val queue = ArrayDeque(listOf(start))
        val visited = mutableSetOf<State>()
        val back = hashMapOf<State, State>()


        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()

            if (!visited.add(cur)) continue

            next(cur).filter { it !in visited }.also {
                back.putAll(it.associateWith { cur }.filterKeys { it !in back })
                queue.addAll(it)
            }
        }

        return SingleSourceBacktrack(back)
    }

    inline fun <State> bfsPath(start: State, crossinline isEnd: (State) -> Boolean, next: (State) -> Iterable<State>): BfsResult<State> {
        if (isEnd(start)) return BfsResult(listOf(start))
        return bfsAllPaths(start, next).getPath { isEnd(it) }
    }

    inline fun <State> bfsPath(start: State, end: State, next: (State) -> Iterable<State>): BfsResult<State> {
        return bfsAllPaths(start, next).getPath(end)
    }

    inline fun <State> bfsPath(start: Iterable<State>, noinline isEnd: (State) -> Boolean, next: (State) -> Iterable<State>): BfsResult<State>? {
        var min: BfsResult<State>? = null

        for (s in start) {
            val path = bfsAllPaths(s, next).getPath(isEnd)
            if (min == null || path.length < min.length) min = path
        }

        return min
    }

    inline fun <State> bfsPath(start: Iterable<State>, end: State, next: (State) -> Iterable<State>): BfsResult<State>? {
        var min: BfsResult<State>? = null

        for (s in start) {
            val path = bfsAllPaths(s, next).getPath(end)
            if (min == null || (path.length != -1 && path.length < min.length)) min = path
        }

        return min
    }

    inline fun <State> dijkstraAllPaths(start: State, next: (State) -> Iterable<Weighted<State>>): SingleSourceWeightedBacktrack<State> {
        val queue = PriorityQueue<Weighted<State>>(compareBy { it.weight })
        val visited = mutableSetOf<State>()
        val back = hashMapOf<Weighted<State>, Weighted<State>>()

        queue.add(Weighted(start, 0))

        while (queue.isNotEmpty()) {
            val (cur, cost) = queue.remove()

            if (!visited.add(cur)) continue

            next(cur).filter { it.state !in visited }.also {
                back.putAll(it.associateWith { Weighted(cur, cost + it.weight) }.filterKeys { it !in back })
                queue.addAll(it)
            }
        }

        return SingleSourceWeightedBacktrack(back)
    }

    inline fun <State> dijkstraPath(start: State, crossinline isEnd: (State) -> Boolean, next: (State) -> Iterable<Weighted<State>>): DijkstraResult<State> {
        if (isEnd(start)) return DijkstraResult(listOf(start), 0)
        return dijkstraAllPaths(start, next).getPath { isEnd(it) }
    }

    inline fun <State> dijkstraPath(start: State, end: State, next: (State) -> Iterable<Weighted<State>>): DijkstraResult<State> {
        return dijkstraAllPaths(start, next).getPath(end)
    }

    inline fun <State> dijkstraPath(start: Iterable<State>, noinline isEnd: (State) -> Boolean, next: (State) -> Iterable<Weighted<State>>): DijkstraResult<State>? {
        var min: DijkstraResult<State>? = null

        for (s in start) {
            val path = dijkstraAllPaths(s, next).getPath(isEnd)
            if (min == null || path.length < min.length) min = path
        }

        return min
    }

    inline fun <State> dijkstraPath(start: Iterable<State>, end: State, next: (State) -> Iterable<Weighted<State>>): DijkstraResult<State>? {
        var min: DijkstraResult<State>? = null

        for (s in start) {
            val path = dijkstraAllPaths(s, next).getPath(end)
            if (min == null || (path.length != -1 && path.length < min.length)) min = path
        }

        return min
    }
}