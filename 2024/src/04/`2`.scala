package `04`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val x = Array(
    (-1,-1), ( 1,-1),
    (-1, 1), ( 1, 1),
  )

  for (src <- srcs) {
    val w = src.getLines().toArray
    (for {
      r <- 1 until w.length - 1
      c <- 1 until w(r).length - 1
      if w(r)(c) == 'A'
      diags = x.map{case (ro, co) => w(r + ro)(c + co)}
      if diags.count(_ == 'S') == 2 && diags.count(_ == 'M') == 2
      if diags(1) != diags(2) // 🤦
    } yield {
      1
    })
      .sum
      .tap(println)
  }
}