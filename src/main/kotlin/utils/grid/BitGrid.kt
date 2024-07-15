package utils.grid

import utils.grid.Grid.Companion.deepCopy
import utils.grid.Grid.Companion.gameOfLife
import utils.point.Point
import utils.toBitSet
import java.util.*

/**
 * Efficient implementation of a boolean 2d grid.
 * This is just a wrapper class over List<BitSet> with extra utils.
 * @property data the data of the grid.
 */
data class BitGrid(var data: MutableList<BitSet> = mutableListOf()): Collection<BitSet> {



    /**
     * Returns the height of the grid (number of rows).
     * @return the height of the grid.
     */
    val height = data.size

    /**
     * Returns the width of the grid (max number of columns that any row has).
     * @return the width of the grid.
     */
    val width = data.maxOfOrNull { it.length() } ?: 0


    /**
     * Ensures that the grid has at least the specified number of rows and columns.
     * @param row the minimum required row.
     * @param col the minimum required col.
     */
    private fun ensureCapacity(row: Int = 0, col: Int = 0) {
        while (data.size < row) {
            data.add(BitSet(col))
        }
    }

    /**
     * Returns the element at the specified Point.
     * @param pos the position to get.
     * @return the element at the specified Point.
     */
    operator fun get(pos: Point) = data[pos.x][pos.y]

    /**
     * Returns the BitSet of the specified row.
     * @param row the row to get.
     * @return the BitSet of the specified row.
     */
    fun getRow(row: Int) = data[row]

    /**
     * Returns the BitSet of the specified column.
     * @param col the column to get.
     * @return the BitSet of the specified column.
     */
    fun getColumn(col: Int) = data.map { it[col] }.toBitSet()

    /**
     * Returns the subgrid specified by the given row range.
     * @param rows the range of rows to get.
     * @return the subgrid specified by the given row range.
     */
    fun getRows(rows: IntRange) = BitGrid(data.subList(rows.first, rows.last + 1))

    /**
     * Returns the element at the specified Point.
     * @param pos the position to get.
     * @return the element at the specified Point.
     */
    operator fun set(pos: Point, value: Boolean) {
        ensureCapacity(pos.x  + 1, pos.y + 1)
        data[pos.x][pos.y] = value
    }

    fun setRow(row: Int, value: Boolean) {
        ensureCapacity(row + 1)
        val rowSize = data[row].size()
        if (value)
            data[row].set(0, rowSize)
        else
            data[row].clear(0, rowSize)
    }

    fun setRow(row: Int, values: BitSet) {
        ensureCapacity(row + 1, values.length())
        data[row] = values
    }

    fun setColumn(col: Int, value: Boolean) {
        ensureCapacity(col = col + 1)
        data.forEach { it[col] = value }
    }

    fun setColumn(col: Int, values: BitSet) {
        ensureCapacity(values.length(), col + 1)
        data.forEachIndexed { index, bitSet -> bitSet[col] = values[index] }
    }

    /**
     * The number of bits in the grid.
     * @return the number of bits in the grid.
     */
    override val size get() = data.sumOf { it.length() }

    /**
     * Returns true if the grid is completely empty.
     * @return true if the grid is empty.
     */
    override fun isEmpty() = data.isEmpty()

    /**
     * Returns an iterator over the BitSet rows.
     * @return an iterator over the BitSet rows.
     */
    override fun iterator() = data.iterator()

    /**
     * Returns true if the grid contains all of the provided BitSet rows.
     * @param elements the BitSet rows to check.
     * @return true if the grid contains all of the provided BitSet rows.
     */
    override fun containsAll(elements: Collection<BitSet>) = data.containsAll(elements)

    /**
     * Returns true if the grid contains the provided BitSet row.
     * @param element the BitSet row to check.
     * @return true if the grid contains the provided BitSet row.
     */
    override fun contains(element: BitSet) = data.contains(element)

    fun deepCopy(): BitGrid {
        val newData = data.map { it.clone() as BitSet }.toMutableList()
        return BitGrid(newData)
    }

    /**
     * Applies the Game of Life rules to the grid.
     * @param rules the transformation function to apply to each point.
     * @param limit the number of steps to simulate. If not provided, the function will return the grid after it stabilizes.
     * @return the grid after applying the Game of Life rules.
     */
    fun gameOfLife(rules: (Boolean, Int) -> Boolean = { it, n -> (it && (n == 2 || n == 3)) || (!it && n == 3) }, limit: Int? = null,): BitGrid {
        var currentGrid = this
        var previousGrid: BitGrid
        var steps = 0
        do {
            previousGrid = currentGrid.deepCopy()
            val newGrid = currentGrid.deepCopy()
            for (row in 0 until height) {
                for (col in 0 until width) {
                    val p = Point(row, col)
                    newGrid[p] = rules(this[p], getNeighbors(p).count { it })
                }
            }
            currentGrid = newGrid
            steps++
            if (limit != null && steps >= limit) break
        } while (currentGrid != previousGrid)
        return currentGrid
    }

    fun getCardinalNeighbors(point: Point) = point.getCardinalNeighbors().map { this[it] }
    fun getNeighbors(point: Point) = point.getNeighbors().map { this[it] }

    companion object {
        fun of(llb: List<List<Boolean>>) = BitGrid(llb.map { list ->
            val bitSet = BitSet(list.size)
            list.forEachIndexed { index, value ->
                bitSet.set(index, value)
            }
            bitSet
        }.toMutableList())
    }
}
