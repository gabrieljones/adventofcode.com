package `04`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs = Seq("example", "input").map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  val Pattern = """Card +(\d+): +(.+) +\| +(.+)""".r

  for {
    lines <- entries
  } {
    lines.map {
      case Pattern(n, p1, p2) =>
        val c = p1.split(" +").map(_.toInt).toSet
        val ns = p2.split(" +").map(_.toInt).toSet
        (n, c, ns)
    }
      .map { case (n, c, ns) => c.intersect(ns)
//        .tap(println)
      }
      .map(_.size)
      .filterNot(_ == 0)
      .map(_ - 1)
      .map(1 << _
//        .tap(println)
      )
      .sum
      .tap(println)
  }
}