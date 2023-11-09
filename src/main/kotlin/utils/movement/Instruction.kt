package utils.movement

import utils.Utils.abs
import utils.Utils.containsNumber
import utils.Utils.extractLetters
import utils.Utils.extractNumbers
import kotlin.math.sign

data class Instruction(val steps: Int, var direction: Direction) {
    fun perform(action: (Int) -> Unit) = repeat(if(steps.sign > 0) steps else steps.abs().also { direction += Direction.SOUTH }) { action(it) }
    companion object {
        fun of(input: String): Instruction {
            if(!input.containsNumber() || input.extractLetters().length != 1) {
                throw IllegalArgumentException("Invalid instruction: $input")
            }
            val steps = input.extractNumbers().toInt()
            val direction = input.extractLetters().first()
            return Instruction(steps, Direction.of(direction))
        }
    }
}
