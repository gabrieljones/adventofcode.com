package `11`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "example2",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    var stones: Iterator[Long] = src.getLines().next().split(" ").map(_.toLong)
      .tap(_.toSeq.tap(println))
      .iterator

    for(_ <- 0 until 25) {
      stones = stones
        .flatMap {
          case 0 => Seq(1L)
          case n if n.toString.length % 2 == 0 =>
            n.toString.pipe(s => Seq(
              s.take(s.length / 2).toLong,
              s.drop(s.length / 2).toLong,
            ))
          case n => Seq(n * 2024)
        }
        .iterator
    }
    stones
      .size.tap(println)
  }
}