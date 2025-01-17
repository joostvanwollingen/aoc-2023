package nl.vanwollingen.aoc.util

import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

abstract class Puzzle(val printDebug: Boolean = false, val exampleInput: Boolean = false) {

    val input by lazy {
        val year = this::class.java.packageName.split(".").last().substring(3)
        val day = this::class.simpleName!!.substring(3)
        PuzzleInputUtil.load(
            "$year/Day${day.toString().padStart(2, '0')}${
                if (exampleInput) {
                    ".example"
                } else {
                    ""
                }
            }.input"
        )
    }

    open fun parseInput(): Any {
        TODO("Not yet implemented")
    }

    abstract fun part1(): Any
    abstract fun part2(): Any

    fun solve() {
        setDebug(printDebug)
        val (r1, t1) = solvePart1()
        log("Part 1: $r1 (${t1.inWholeMicroseconds} μs)")

        val (r2, t2) = solvePart2()
        log("Part 2: $r2 (${t2.inWholeMicroseconds} μs)")
    }

    fun solvePart1() = runTimed { part1() }
    fun solvePart2() = runTimed { part2() }

    private fun <T> runTimed(job: () -> T): TimedValue<T> = measureTimedValue { job() }

    companion object {
        private var printDebug = false

        fun setDebug(printDebug: Boolean) {
            this.printDebug = printDebug
        }

        fun log(message: Any, linebreak: Boolean = true) {
            print("${message}${if (linebreak) "\n" else ""}")
        }

        fun debug(message: Any, linebreak: Boolean = true) {
            if (printDebug) print("${message}${if (linebreak) "\n" else ""}")
        }

        fun debug(message: String? = null, func: () -> Unit) {
            if (printDebug) {
                message?.let { println(it) }
                func()
            }
        }
    }
}