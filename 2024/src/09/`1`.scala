package `09`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val line = src.getLines().next() + "0"

    val zipped = line.map(_.asDigit).grouped(2).zipWithIndex

    val blocks = zipped.foldLeft(Seq.empty[Int]) {
      case (acc, (Seq(a, b), i)) => acc ++ Seq.fill(a)(i) ++ Seq.fill(b)(-1)
    }
      .toArray

    var t = blocks.length - 1
    var h = 0
    while (h < t) {
      println(s"h: $h, t: $t")
      (blocks(h), blocks(t)) match {
        case (_, -1) => //tail is empty
          t -= 1 //advance tail pointer towards center
        case (hv @ -1, tv) => //head is empty and tail is populated
          blocks(h) = tv; blocks(t) = hv // swap head and tail
        case (_, _) => //head is populated
          h += 1 //advance head pointer towards center
      }
    }

    blocks.mkString.tap(println)

    blocks.takeWhile(_ != -1)
      .zipWithIndex
      .map{ case (e, i) => e.toLong*i }
      .sum
      .tap(println)

  }
}