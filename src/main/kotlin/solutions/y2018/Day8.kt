package solutions.y2018

import Solution
import java.io.File
import utils.*

class Day8 : Solution(2018) {

    data class Node(var sizeChildren: Int, var sizeMetadata: Int, var children: List<Node>, var metadata: List<Int>)

    override fun solvePart1(input: File) = parseTree(input.text.split().map(String::toInt).iterator()).getMetadataSum()

    override fun solvePart2(input: File) = parseTree(input.text.split().map(String::toInt).iterator()).getValue()

    private fun Node.getMetadataSum(): Int = this.metadata.sum() + this.children.sumOf { it.getMetadataSum() }

    private fun Node.getValue(): Int = if (sizeChildren == 0) metadata.sum() else {
        metadata.sumOf { children.getOrNull(it - 1)?.getValue() ?: 0 }
    }

    private fun parseTree(license: Iterator<Int>): Node {
        val sizeChildren = license.next()
        if (sizeChildren == 0) {
            val sizeMetadata = license.next()
            return Node(0, sizeMetadata, emptyList(), license.next(sizeMetadata))
        } else {
            val sizeMetadata = license.next()
            val res = Node(sizeChildren, sizeMetadata, (1..sizeChildren).map { parseTree(license) }, license.next(sizeMetadata))
            return res
        }
    }
}