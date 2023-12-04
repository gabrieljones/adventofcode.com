package `03`

import java.io.File
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
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
        schematic.zipWithIndex.flatMap { case (line, y) =>
          //regex all numbers and their index
          regex.findAllMatchIn(line).map { m =>
            val x = m.start
            val value = m.group(1)
            (x, y, value)
          }
          .filterNot { case (x, y, value) =>
            //get all adjacent coordinates of the bounding box of value
            val box = for {
              x <- x - 1 to x + value.length
              y <- y - 1 to y + 1
            } yield (x, y)

            box.forall { case (x, y) => scala.util.Try(schematic(y)(x).pipe(c => c.isDigit || c == '.')).getOrElse(true) }
          }
        }
          .tap(println)
          .map(_._3.toInt)
          .sum
          .tap(println)
      }
  }
}