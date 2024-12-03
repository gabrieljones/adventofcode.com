package `03`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val pattern = """mul\(\d+,\d+\)""".r

  for (src <- srcs) {
    val lines    = src.getLines()
    val matches = lines.flatMap(pattern.findAllIn(_))

    matches
//      .tap(_.foreach(println))
      .map(_.drop(4).dropRight(1).split(",").map(_.toLong))
      .map{ case Array(a, b) => a * b}
      .sum
      .tap(println)

  }
}