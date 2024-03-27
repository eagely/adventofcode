package utils.movement

import java.util.*

/**
 * Generic path finding utils.
 * Mainly designed for grids.
 */
object PathFinding {
    data class Weighted<out State>(val state: State, val weight: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Weighted<*>

            if (state != other.state) return false
            if (weight != other.weight) return false

            return true
        }

        override fun hashCode(): Int {
            var result = state?.hashCode() ?: 0
            result = 31 * result + weight
            return result
        }
    }

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
        val start get() = path.firstOrNull()
        val end get() = path.lastOrNull()
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

    data class SingleSourceWeightedBacktrack<State>(
        private val back: Map<State, State>,
        private val costToCome: Map<State, Int>
    ) {
        fun getPath(end: State): DijkstraResult<State> {
            val path = mutableListOf<State>()
            var current = end
            while (back.containsKey(current)) {
                path.add(current)
                current = back[current]!!
            }
            path.add(current) // Add the start state
            path.reverse()
            return DijkstraResult(path, costToCome[end] ?: 0)
        }

        fun getPath(isEnd: (State) -> Boolean) = back.keys.filter { isEnd(it) }.map { getPath(it) }.sortedBy { it.length }.first { it.length != -1 }
    }

    inline fun <State> bfsAllPaths(start: State, next: (State) -> Iterable<State>): SingleSourceBacktrack<State> {
        val queue = ArrayDeque(listOf(start))
        val visited = hashSetOf<State>()
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
        if (start == end) return BfsResult(listOf(start))
        return bfsAllPaths(start, next).getPath(end)
    }

    inline fun <State> bfsPath(start: Iterable<State>, noinline isEnd: (State) -> Boolean, next: (State) -> Iterable<State>): BfsResult<State>? {
        var min: BfsResult<State>? = null

        for (s in start) {
            val path = bfsPath(s, isEnd, next)
            if (min == null || path.length < min.length) min = path
        }

        return min
    }

    inline fun <State> bfsPath(start: Iterable<State>, end: State, next: (State) -> Iterable<State>) = BfsResult(bfsPath(end, isEnd = { it in start }, next).path.reversed())

    inline fun <State> bfsCost(start: State, crossinline isEnd: (State) -> Boolean, next: (State) -> Iterable<State>): Int {
        val queue = ArrayDeque(listOf(start))
        val visited = hashSetOf<State>()
        val cost = hashMapOf<State, Int>().apply { this[start] = 0 }

        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            val curCost = cost[cur]!!

            if (isEnd(cur)) return curCost

            if (!visited.add(cur)) continue

            next(cur).filter { it !in visited }.also {
                it.forEach { queue.add(it) }
                it.forEach { cost[it] = curCost + 1 }
            }
        }

        return -1
    }

    inline fun <State> bfsCost(start: State, end: State, next: (State) -> Iterable<State>) = bfsCost(start, isEnd = { it == end }, next)

    inline fun <State> dijkstraAllPaths(start: State, next: (State) -> Iterable<Weighted<State>>): SingleSourceWeightedBacktrack<State> {
        val queue = PriorityQueue<Weighted<State>>(compareBy { it.weight })
        val visited = hashMapOf<State, Int>()
        val back = hashMapOf<State, State>()
        val costToCome = hashMapOf<State, Int>()
        queue.add(Weighted(start, 0))
        visited[start] = 0

        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current.weight > visited[current.state]!!) continue

            for (neighbor in next(current.state)) {
                val nextCost = visited[current.state]!! + neighbor.weight
                if (nextCost < visited.getOrDefault(neighbor.state, Int.MAX_VALUE)) {
                    visited[neighbor.state] = nextCost
                    queue.add(Weighted(neighbor.state, nextCost))
                    back[neighbor.state] = current.state
                    costToCome[neighbor.state] = nextCost
                }
            }
        }

        return SingleSourceWeightedBacktrack(back, costToCome)
    }



    inline fun <State> dijkstraPath(start: State, crossinline isEnd: (State) -> Boolean, next: (State) -> Iterable<Weighted<State>>): DijkstraResult<State> {
        if (isEnd(start)) return DijkstraResult(listOf(start), 0)
        return dijkstraAllPaths(start, next).getPath { isEnd(it) }
    }

    inline fun <State> dijkstraPath(start: State, end: State, next: (State) -> Iterable<Weighted<State>>): DijkstraResult<State> {
        if (start == end) return DijkstraResult(listOf(start), 0)
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

    inline fun <State> dijkstraCost(start: State, crossinline isEnd: (State) -> Boolean, next: (State) -> Iterable<Weighted<State>>): Int {
        val queue = PriorityQueue<Weighted<State>>(compareBy { it.weight })
        val visited = hashMapOf<State, Int>().apply { this[start] = 0 }
        queue.add(Weighted(start, 0))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val currentState = current.state
            val currentCost = current.weight

            if (isEnd(currentState)) return currentCost
            if (currentCost > visited[currentState]!!) continue

            for (neighbor in next(currentState)) {
                val nextCost = currentCost + neighbor.weight
                if (nextCost < visited.getOrDefault(neighbor.state, Int.MAX_VALUE)) {
                    visited[neighbor.state] = nextCost
                    queue.offer(Weighted(neighbor.state, nextCost))
                }
            }
        }

        return -1
    }


    inline fun <State> dijkstraCost(start: State, end: State, next: (State) -> Iterable<Weighted<State>>) = dijkstraCost(start, isEnd = { it == end }, next)

    inline fun <State> dijkstraPath(start: Iterable<State>, end: State, next: (State) -> Iterable<Weighted<State>>) = dijkstraPath(end, isEnd = { it in start }, next).let { DijkstraResult(it.path.reversed(), it.cost) }
}