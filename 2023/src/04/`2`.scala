package `04`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs = Seq("example", "input").map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  val Pattern = """Card +(\d+): +(.+) +\| +(.+)""".r

//  def count(cs: IndexedSeq[Int], i: Int): Int = {
//    val f = cs(i)
//    if (f == 0) return 0
//    (1 to f).map(iF => count(cs, i+iF)).sum
//  }

  for {
    lines <- entries
  } {
    lines.map {
        case Pattern(n, p1, p2) =>
          val c = p1.split(" +").map(_.toInt).toSet
          val ns = p2.split(" +").map(_.toInt).toSet
          (n, c, ns)
      }
      .map { case (n, c, ns) => c.intersect(ns) }
      .map(_.size)
      .zipWithIndex
      .toIndexedSeq
      .pipe(cards =>
        val deck = mutable.Stack[(Int, Int)]()
        deck.prependAll(cards)
        val discard = mutable.ListBuffer[(Int, Int)]()
        while (deck.nonEmpty) {
//          println(deck)
          val (m, n) = deck.pop()
          discard.append((m, n))
          if (m > 0) for {
            j <- m to 1 by -1
          } {
            deck.push(cards(n+j))
          }
        }

        discard.size
          .tap(println)
      )
  }
}