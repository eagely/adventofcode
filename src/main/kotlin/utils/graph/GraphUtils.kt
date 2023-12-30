package utils.graph

import org.jgrapht.Graph
import org.jgrapht.graph.*
import utils.point.Point

object GraphUtils {
    fun <V, E> Graph<V, E>.getNeighbors(vertex: V) = this.edgesOf(vertex).map { this.getEdgeTarget(it) }.toSet()

    operator fun <V, E> Graph<Vertex<V>, E>.get(position: Point) = this.vertexSet().find { it.position == position }
    operator fun <V, E> Graph<Vertex<V>, E>.get(value: V) = this.vertexSet().find { it.value == value }

    fun <V, E> Graph<V, E>.addEdges(edges: List<Pair<V, V>>, weight: (V, V) -> Double? = { _, _ -> null }) = edges.forEach { (source, target) -> this.addEdge(source, target)?.let { e -> weight(source, target)?.let { setEdgeWeight(e, it) } } }

    fun <V, E> Graph<V, E>.addVertices(vertices: List<V>) = vertices.forEach { this.addVertex(it) }

    fun simpleGraphOf(input: List<String>, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = SimpleGraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun simpleDirectedGraphOf(input: List<String>, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = SimpleDirectedGraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun simpleWeightedGraphOf(input: List<String>, weight: (Vertex<Char>, Vertex<Char>) -> Double? = { _, _ -> null }) = SimpleWeightedGraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { weight(s, it) != null }.map { s to it } }, weight)
    }

    fun simpleDirectedWeightedGraphOf(input: List<String>, weight: (Vertex<Char>, Vertex<Char>) -> Double? = { _, _ -> null }) = SimpleDirectedWeightedGraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { weight(s, it) != null }.map { s to it } }, weight)
    }

    fun multigraphOf(input: List<String>, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = Multigraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun directedMultigraphOf(input: List<String>, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = DirectedMultigraph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun weightedMultigraphOf(input: List<String>, weight: (Vertex<Char>, Vertex<Char>) -> Double? = { _, _ -> null }) = WeightedMultigraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { weight(s, it) != null }.map { s to it } }, weight)
    }

    fun directedWeightedMultigraphOf(input: List<String>, weight: (Vertex<Char>, Vertex<Char>) -> Double? = { _, _ -> null }) = DirectedWeightedMultigraph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { weight(s, it) != null }.map { s to it } }, weight)
    }

    fun pseudoGraphOf(input: List<String>, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = Pseudograph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun directedPseudographOf(input: List<String>, hasEdge: (Vertex<Char>, Vertex<Char>) -> Boolean = { _, _ -> true }) = DirectedPseudograph<Vertex<Char>, DefaultEdge>(DefaultEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { hasEdge(s, it) }.map { s to it } })
    }

    fun weightedPseudographOf(input: List<String>, weight: (Vertex<Char>, Vertex<Char>) -> Double? = { _, _ -> null }) = WeightedPseudograph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { weight(s, it) != null }.map { s to it } }, weight)
    }

    fun directedWeightedPseudographOf(input: List<String>, weight: (Vertex<Char>, Vertex<Char>) -> Double? = { _, _ -> null }) = DirectedWeightedPseudograph<Vertex<Char>, DefaultWeightedEdge>(DefaultWeightedEdge::class.java).apply {
        addVertices(input.flatMapIndexed { x, row -> row.mapIndexed { y, c -> Vertex(Point(x, y), c) } })
        addEdges(vertexSet().flatMap { s -> s.position.getCardinalNeighbors().mapNotNull { this[it] }.filter { weight(s, it) != null }.map { s to it } }, weight)
    }
}