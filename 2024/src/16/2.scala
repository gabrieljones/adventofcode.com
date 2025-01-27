package `16`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

import lib.*

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val moveTable = Map(
    '<' -> (-1, 0),
    '>' -> ( 1, 0),
    '^' -> ( 0,-1),
    'v' -> ( 0, 1),
  )

  for (src <- srcs) {
  }
}
