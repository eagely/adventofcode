package utils.graph

import org.jgrapht.Graph
import org.jgrapht.graph.*
import utils.Utils.p
import utils.point.Point

@Suppress("unused", "MemberVisibilityCanBePrivate")
object GraphUtils {
    fun <V, E> Graph<V, E>.getNeighbors(vertex: V) = this.edgesOf(vertex).map { this.getEdgeTarget(it) }.toSet()

    operator fun <V, E> Graph<Vertex<V>, E>.get(position: Point) = this.vertexSet().find { it.position == position }
    operator fun <V, E> Graph<Vertex<V>, E>.get(value: V) = this.vertexSet().find { it.value == value }

    fun <V, E> Graph<V, E>.addEdges(edges: Collection<Pair<V, V>>, weight: (V, V) -> Double? = { _, _ -> null }) = this.apply { edges.forEach { (source, target) -> this.addEdge(source, target)?.let { e -> weight(source, target)?.let { setEdgeWeight(e, it) } } } }
    fun <V, E> Graph<V, E>.addVertices(vertices: Collection<V>) = this.apply { vertices.forEach { this.addVertex(it) } }
    fun <V, E> Graph<V, E>.addSelfLoops(vertices: Collection<V> = this.vertexSet()) = this.apply { vertices.forEach { this.addEdge(it, it) } }
    fun <V, E> Graph<V, E>.getConnection(edge: E, vertex: V) = listOf(this.getEdgeTarget(edge), this.getEdgeSource(edge)).first { it != vertex }!!

    val <V, E> Graph<Vertex<V>, E>.tl get() = this.vertexSet().first { it.position == this.vertexSet().minOf { it.position.x } p this.vertexSet().minOf { it.position.y } }
    val <V, E> Graph<Vertex<V>, E>.br get() = this.vertexSet().first { it.position == this.vertexSet().maxOf { it.position.x } p this.vertexSet().maxOf { it.position.y } }
    val <V, E> Graph<Vertex<V>, E>.tr get() = this.vertexSet().first { it.position == this.vertexSet().minOf { it.position.x } p this.vertexSet().maxOf { it.position.y } }
    val <V, E> Graph<Vertex<V>, E>.bl get() = this.vertexSet().first { it.position == this.vertexSet().maxOf { it.position.x } p this.vertexSet().minOf { it.position.y } }

    fun simpleGraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = SimpleGraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun simpleDirectedGraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = SimpleDirectedGraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun simpleWeightedGraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, weight: (Vertex<Char>, Vertex<Char>) -> Double = { _, _ -> 1.0 }) = SimpleWeightedGraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { weight(s, it) != -1.0 }.map { s to it } }, weight)
    }

    fun simpleDirectedWeightedGraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, weight: (Vertex<Char>, Vertex<Char>) -> Double = { _, _ -> 1.0 }) = SimpleDirectedWeightedGraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { weight(s, it) != -1.0 }.map { s to it } }, weight)
    }

    fun multigraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = Multigraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun directedMultigraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = DirectedMultigraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun weightedMultigraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, weight: (Vertex<Char>, Vertex<Char>) -> Double = { _, _ -> 1.0 }) = WeightedMultigraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { weight(s, it) != -1.0 }.map { s to it } }, weight)
    }

    fun directedWeightedMultigraphOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, weight: (Vertex<Char>, Vertex<Char>) -> Double = { _, _ -> 1.0 }) = DirectedWeightedMultigraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { weight(s, it) != -1.0 }.map { s to it } }, weight)
    }

    fun pseudographOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = Pseudograph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun directedPseudographOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = DirectedPseudograph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun weightedPseudographOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, weight: (Vertex<Char>, Vertex<Char>) -> Double = { _, _ -> 1.0 }) = WeightedPseudograph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { weight(s, it) != -1.0 }.map { s to it } }, weight)
    }

    fun directedWeightedPseudographOf(input: List<String>, next: (Point) -> Collection<Point> = Point::getCardinalNeighbors, weight: (Vertex<Char>, Vertex<Char>) -> Double = { _, _ -> 1.0 }) = DirectedWeightedPseudograph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> next(s.position).mapNotNull { this[it] }.filter { weight(s, it) != -1.0 }.map { s to it } }, weight)
    }
}