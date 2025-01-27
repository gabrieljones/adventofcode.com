package `11`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2simulate` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "example2",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    var stones: Array[Long] = src.getLines().next().split(" ").map(_.toLong)
      .tap(_.toSeq.tap(println))

    for (
      iters <- 0 until 75
    ) {
      val start = System.nanoTime()
      var stonesI = stones.iterator
      for (_ <- 0 until iters) {
        stonesI = stonesI
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
      val size = stonesI.size
      java.time.Duration.of(System.nanoTime() - start, java.time.temporal.ChronoUnit.NANOS)
        .tap(d => println(f"$iters%3d: $size%10d $d"))
    }
  }
}