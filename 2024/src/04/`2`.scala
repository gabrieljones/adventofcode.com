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
    val w      = src.getLines().toArray
    val rcs    = for {
      r <- (1 until w.length - 1).iterator
      c <- (1 until w(0).length - 1).iterator
    } yield (r, c)
    
    rcs
      .filter { case (r, c) => w(r)(c) == 'A'}
      .map { case (r, c) => x.map { case (ro, co) => w(r + ro)(c + co) } }
      .count(diags =>
        diags.count(_ == 'S') == 2 &&
        diags.count(_ == 'M') == 2 &&
        diags(1) != diags(2)
      )
      .tap(println)
  }
}