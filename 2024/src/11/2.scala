package `11`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "example2",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val memo = mutable.Map.empty[(Long, Int), Seq[Long]]
/*
  def blink(rem: Int)(e: Long): Long = {
    if (rem <= 0) return e
    memo.get((e, rem)) match {
      case Some(v) => v
      case None =>
        val result: Seq[Long] = e match {
          case 0 => Seq(1)
          case n if n.toString.length % 2 == 0 =>
            val s = n.toString
            Seq(s.take(s.length / 2).toLong, s.drop(s.length / 2).toLong)
          case n => Seq(n * 2024)
        }
        val sum = result.map(blink(rem - 1)).sum
        memo.put((e, rem), sum)
        sum
    }
  }

  for (src <- srcs) {
    var stones: Array[Long] = src.getLines().next().split(" ").map(_.toLong)
      .tap(_.toSeq.tap(println))

    for (
      iters <- 1 to 75
    ) {
      val start = System.nanoTime()
      val stonesQ = stones.map(e => blink(e, iters))
        .tap(_.toSeq.tap(println))
      val size = stonesQ.sum
      java.time.Duration.of(System.nanoTime() - start, java.time.temporal.ChronoUnit.NANOS)
        .tap(d => println(f"$iters%3d: $size%10d $d"))
    }
  }
  */
}