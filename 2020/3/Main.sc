import java.io.File

import scala.io.Source

val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2020")
val input = new File(workingDir, "/3/input")
println(input.getAbsolutePath)

def src = Source.fromFile(input)

def traverse(r: Int, d: Int) = for {
  (lines, i) <- src.getLines().grouped(d).zipWithIndex
  line = lines.head
  row = Iterator.continually(line).flatten
  c = row.drop(i*r).next
} yield {
  c
}

//Part 1
traverse(3,1).count(_ == '#')

//Part 2
val slopes = Seq(
  (1,1),
  (3,1),
  (5,1),
  (7,1),
  (1,2),
)

val encounterCounts = for {
  (r, d) <- slopes
  encounters = traverse(r, d)
  count = encounters.count(_ == '#')
  countBigInt = BigInt(count)
} yield {
  countBigInt
}

encounterCounts.product

Source.fromFile(input)
  .getLines()
  .zipWithIndex
  .map { case (line, i) =>
    Iterator.continually(line)
      .flatten
      .drop(i * 3)
      .next
  }
  .count(_ == '#')

Seq(
  (1,1),
  (3,1),
  (5,1),
  (7,1),
  (1,2),
)
  .map { case (r, d) =>
    Source.fromFile(input)
      .getLines()
      .grouped(d)
      .zipWithIndex
      .map { case (lines, i) =>
        Iterator.continually(lines.head)
          .flatten
          .drop(i * r)
          .next
      }
  }
  .map(_.count(_ == '#'))
  .map(BigInt.apply)
  .product
