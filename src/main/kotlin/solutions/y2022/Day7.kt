package solutions.y2022

import Solution
import java.io.File

class Day7 : Solution(2022) {
    override fun solvePart1(input: File): String {
        val root = parseInput(input.readLines())
        val directories = mutableListOf<Directory>()
        fun collectDirs(dir: Directory) {
            directories.add(dir)
            for (child in dir.children) {
                if (child is Directory) {
                    collectDirs(child)
                }
            }
        }
        collectDirs(root)

        return directories.filter { it.totalSize() <= 100000 }.sumBy { it.totalSize() }.toString()
    }

    override fun solvePart2(input: File): String {
        val root = parseInput(input.readLines())
        val directories = mutableListOf<Directory>()
        fun collectDirs(dir: Directory) {
            directories.add(dir)
            for (child in dir.children) {
                if (child is Directory) {
                    collectDirs(child)
                }
            }
        }
        collectDirs(root)
        return directories.filter { it.totalSize() >= root.totalSize() - 40000000 }.minOfOrNull { it.totalSize() }.toString()
    }

    fun parseInput(input: List<String>): Directory {
        val root = Directory("/")
        var currentDir = root
        val dirStack = mutableListOf<Directory>()
        dirStack.add(root)

        for (line in input) {
            when {
                line.startsWith("$ cd ") -> {
                    val dirName = line.split(" ")[2]
                    when {
                        dirName == ".." -> {
                            dirStack.removeAt(dirStack.size - 1)
                            currentDir = dirStack.last()
                        }
                        dirName == "/" -> {
                            dirStack.clear()
                            dirStack.add(root)
                            currentDir = root
                        }
                        else -> {
                            val newDir = Directory(dirName)
                            currentDir.children.add(newDir)
                            dirStack.add(newDir)
                            currentDir = newDir
                        }
                    }
                }
                line.startsWith("dir ") -> {
                    val dirName = line.split(" ")[1]
                    val dir = Directory(dirName)
                    currentDir.children.add(dir)
                }
                else -> {
                    if (!line.startsWith("$")) {
                        val parts = line.split(" ")
                        val fileSize = parts[0].toInt()
                        val fileName = parts[1]
                        val file = AocFile(fileName, fileSize)
                        currentDir.children.add(file)
                    }
                }

            }
        }

        return root
    }


    open class Node(val name: String)

    class AocFile(name: String, val size: Int) : Node(name)

    class Directory(name: String) : Node(name) {
        val children = mutableListOf<Node>()
        fun totalSize(): Int = children.sumBy {
            if (it is AocFile) it.size else (it as Directory).totalSize()
        }
    }

}

