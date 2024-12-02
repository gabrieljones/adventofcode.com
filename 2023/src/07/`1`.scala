package `07`

import java.io.File
import java.lang.Math.log10
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  for {
    lines: Iterator[String] <- entries
  } {

  }
}
