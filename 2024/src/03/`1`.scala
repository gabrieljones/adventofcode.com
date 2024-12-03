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

  val Pattern = """mul\((\d+),(\d+)\)""".r

  for (src <- srcs) {
    src
      .getLines()
      .flatMap(Pattern.findAllIn(_))
      .map { case Pattern(a, b) => a.toLong * b.toLong}
      .sum
      .tap(println)
  }
}