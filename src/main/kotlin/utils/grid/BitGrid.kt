package utils.grid

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
     * Returns the element at the specified Point.
     * @param pos the position to get.
     * @return the element at the specified Point.
     */
    operator fun get(pos: Point) = data[pos.x][pos.y]

    /**
     * Returns the element at the specified row and column.
     * @param row the row to get.
     * @param col the column to get.
     * @return the element at the specified row and column.
     */
    operator fun get(row: Int, col: Int) = data[row][col]

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
     * Returns the subgrid specified by the given column range.
     * @param cols the range of columns to get.
     * @return the subgrid specified by the given column range.
     */
    //fun getColumns(cols: IntRange) = BitGrid(cols.map { getColumn(it) })

    /**
     * Returns the subgrid specified by the given ranges.
     * @param rows the range of rows to get.
     * @param cols the range of columns to get.
     * @return the subgrid specified by the given ranges.
     */
    //fun getSubGrid(rows: IntRange, cols: IntRange) = getRows(rows).getColumns(cols)

    /**
     * Returns the element at the specified Point.
     * @param pos the position to get.
     * @return the element at the specified Point.
     */
    operator fun set(pos: Point, value: Boolean) {
        data[pos.x][pos.y] = value
    }

    operator fun set(row: Int, col: Int, value: Boolean) {
        data[row][col] = value
    }

    fun setRow(row: Int, value: Boolean) {
        val rowSize = data[row].size()
        if (value)
            data[row].set(0, rowSize)
        else
            data[row].clear(0, rowSize)
    }

    fun setRow(row: Int, values: BitSet) {
        data[row] = values
    }

    fun setColumn(col: Int, value: Boolean) {
        data.forEach { it[col] = value }
    }

    fun setColumn(col: Int, values: BitSet) {
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
}
