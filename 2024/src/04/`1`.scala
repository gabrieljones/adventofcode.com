package `04`

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

  val pad = Iterator.continually(' ')
  
  extension (ws: Array[Array[Char]]) {
    def skew: Array[Array[Char]] = ws.zipWithIndex.map { case (row, i) => (pad.take(i) ++ row.iterator ++ pad).take(ws.length * 2).toArray }
  }

  val XMAS = "XMAS".toArray

  for (src <- srcs) {
    val rows           : Array[Array[Char]]        = src.getLines().toArray.map(_.toArray)
    val transformations: Array[Array[Array[Char]]] = Array(
      rows,
      rows.transpose,
      rows.skew.transpose,
      rows.map(_.reverse).skew.transpose,
    )
    transformations
      .flatMap(_.flatMap(_.sliding(4).filter(t => t.sameElements(XMAS) || t.reverse.sameElements(XMAS))))
      .length
      .tap(println)
  }
}