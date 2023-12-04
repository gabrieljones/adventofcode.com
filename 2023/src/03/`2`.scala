package `03`

import java.io.File
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `2` extends App {
  val workingDir = new File("/Users/gjone12/Projects/adventofcode.com/2023/src")
  val inputs = Seq(
    "example",
    "input",
  )
    .map(p => new File(workingDir, s"/${this.getClass.getPackageName}/$p"))

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  val regex = """(\d+)""".r

  for {
    lines <- entries
  } {
    lines.toIndexedSeq
      .pipe { schematic =>
        //parse all numerical strings, give their coordinates and value
        val entries = schematic.zipWithIndex.flatMap { case (line, y) =>
          //regex all numbers and their index
          regex.findAllMatchIn(line).map { m =>
            val x = m.start
            val value = m.group(1)
            (x, y, value)
          }
        }
        //map all entries to the coordinates of all their characters
        val coordinateEntryMap: Map[(Int, Int), (Int, Int, String)] =
          entries.flatMap { case (x, y, value) => (0 until value.length).map( i => (x + i, y) -> (x, y, value)) }
            .toMap

        //find all asterisk and their coordinates
        val asterisk = schematic.zipWithIndex.flatMap { case (line, y) =>
          line.zipWithIndex.collect {
            case ('*', x) => (x, y)
          }
        }

        //find all entries adjacent to each asterisk
        val adjacentEntries = asterisk.map { case (x, y) =>
          //orthogonally and diagonally adjacent
          val adjacent =
            for {
              x <- x - 1 to x + 1
              y <- y - 1 to y + 1
            } yield (x, y)
//              .tap(println)
          adjacent.flatMap(coordinateEntryMap.get).toSet
        }

        adjacentEntries
          .tap(println)
          .tap(_.size.tap(println))
          .tap(_.count(_.size == 2).tap(println))
          .pipe{
            _.filter(_.size == 2)
              .map(_.map(_._3.toInt).product)
              .sum
          }
          .tap(println)

      }
  }
}
