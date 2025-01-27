package `09`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
//    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val line = src.getLines().next() + "0"

    val zipped = line.map(_.asDigit).grouped(2).zipWithIndex

    var blocks = zipped.foldLeft(Seq.empty[(Int, Int)]) {
      case (acc, (Seq(a, b), i)) => acc ++ Seq(i -> a, (-1) -> b)
    }
      .dropRight(1)

    blocks.reverse.foldLeft(blocks) {
      case (acc, (-1, _)) => blocks //skip over empty blocks
      case (acc, (tv, ts)) =>
        val accI = acc.iterator
        val head = accI.takeWhile { case (hv, hs) => hv != -1 || hs < ts }
        val slot = accI.next()
        val slotFill = Seq(Option((tv, ts)), Option.when(slot._1 == -1)((-1, slot._2 - ts))).flatten
        (head ++ slotFill ++ accI).toSeq
    }

    blocks.mkString.tap(println)

    blocks
      .flatMap{ case (e, i) => Seq.fill(e)(i) }
      .zipWithIndex
      .map{
        case (-1, _) => 0
        case (e, i) => e.toLong*i
      }
      .sum
      .tap(println)

  }
}