import java.io.File
import kotlin.streams.asStream
import kotlin.streams.toList

fun main() {
    val lines = File("day3.input").readLines()
    val (partNumbers: List<PartNumber>, symbols: List<Symbol>) = readGrid(lines)

    //Part 1
    val partNumbersAdjecentToSymbols: List<Pair<PartNumber, List<Symbol>>> = partNumbers.map { part ->
        val partIsAdjecent: List<Point> = part.location.getSurroundingPoints()
        val symbolLocations: List<Point> = symbols.map { symbol -> symbol.location }
        val isValidPartNumber = partIsAdjecent.intersect(symbolLocations)
        part to symbols.filter { symbol -> isValidPartNumber.contains(symbol.location) }
    }.filter {
        it.second.isNotEmpty()
    }
    println(partNumbersAdjecentToSymbols.sumOf { it.first.number })

    //Part 2
    val grouped =
        partNumbersAdjecentToSymbols.groupBy { part -> part.second }.filter { symbol -> symbol.value.size == 2 }
    println(grouped.values.sumOf { part -> part.first().first.number * part.last().first.number })
}

private fun readGrid(lines: List<String>): Pair<List<PartNumber>, List<Symbol>> {
    val partNumbers: MutableList<PartNumber> = mutableListOf()
    val symbols: MutableList<Symbol> = mutableListOf()
    lines.mapIndexed { index, line ->
        partNumbers += getPartNumbersFromLine(index, line)
        symbols += getSymbolsFromLine(index, line)
    }
    return Pair(partNumbers, symbols)
}

fun getSymbolsFromLine(index: Int, line: String): List<Symbol> {
    val matches = Regex("([*#+\$=&%/@-])").findAll(line)
    return matches.asStream().map { match ->
        Symbol(
            match.value, Point(index + 1, match.range.first + 1)
        )
    }.toList()
}

fun getPartNumbersFromLine(index: Int, line: String): List<PartNumber> {
    val matches = Regex("([0-9]+)").findAll(line)
    return matches.asStream().map { match ->
        PartNumber(match.value.toInt(), match.range.map { location -> Point(index + 1, location + 1) })
    }.toList()
}

data class Symbol(val symbol: String, val location: Point)

data class PartNumber(val number: Int, val location: List<Point>)

data class Point(val y: Int, val x: Int)

fun Point.getSurroundingPoints(): List<Point> {
    val points = mutableListOf<Point>()
    for (i in this.x - 1..this.x + 1) {
        for (o in this.y - 1..this.y + 1) {
            points.add(Point(o, i))
        }
    }
    return points.minus(this)
}

fun List<Point>.getSurroundingPoints(): List<Point> {
    return this.asSequence().map { point -> point.getSurroundingPoints() }.flatten().minus(this).toSet()
        .toList()
}