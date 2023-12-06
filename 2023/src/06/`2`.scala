package `06`

import java.io.File
import java.lang.Math.log10
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `2` extends App {
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
    lines.map(_.split(" +"))
      .map(_.drop(1))
      .map(_.tap(_.mkString(",").tap(println)))
      .map(_.mkString)
      .pipe { iter => iter.next().toInt -> iter.next().toLong }
      .tap(println)
      .pipe{ case (t, d) => ( 0 to t ).map(_.toLong).map( x => x * (t - x) ).count( _ > d ) }
      .tap(println)
    }
  }
