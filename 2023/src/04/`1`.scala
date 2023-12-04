package `04`

import java.io.File
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
  val workingDir = new File("/Users/gjone12/Projects/adventofcode.com/2023/src")
  val inputs = Seq("example", "input").map(p => new File(workingDir, s"/${this.getClass.getPackageName}/$p"))

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