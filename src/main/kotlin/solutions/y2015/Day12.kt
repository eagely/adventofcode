package solutions.y2015

import Solution
import com.google.gson.Gson
import com.google.gson.JsonElement
import utils.extractNegativesSeparated
import utils.text
import java.io.File


class Day12 : Solution(2015) {

    override fun solvePart1(input: File) = input.text.extractNegativesSeparated().sum()

    override fun solvePart2(input: File) = Gson().fromJson(input.text, JsonElement::class.java).sum()

    private fun JsonElement.sum(): Int {
        var sum = 0
        if (this.isJsonArray)
            for (e in this.asJsonArray) sum += e.sum()
        else if (this.isJsonObject) {
            val obj = this.asJsonObject
            for (key in obj.keySet()) {
                obj[key].let { if (it.isJsonPrimitive && "red" == it.asString) return 0 }
                sum += (obj[key]).sum()
            }
        } else if (this.isJsonPrimitive && this.asJsonPrimitive.isNumber)
            sum = this.asInt
        return sum
    }
}