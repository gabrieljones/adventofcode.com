package `25`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

import lib.*

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val es = src.getLines().grouped(8)
      .map(_.take(7).transpose)
      .toSeq
      .groupMap(_.head.head)(_.map(_.count(_ == '#') - 1))
    val ls = es('#')
    val ks = es('.')

    (for {
      l <- ls
      k <- ks
    } yield {
      l.zip(k).map((l,k) => l+k).forall(_ < 6)
    })
      .count(identity)
      .tap(println)

  }
}
