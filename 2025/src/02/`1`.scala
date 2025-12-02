package `02`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.language.postfixOps
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {

    val line = src.getLines().next()
    val input = line.split(",").map(_.split("-")).toSeq
      .map{ case Array(s, e) => BigInt(s) to BigInt(e)}
    println(input)
    val rs: Seq[BigInt] =
      for {
        range <- input
        e <- range
        eS = e.toString
        length = eS.length
        if length % 2 == 0
        if eS.take(length / 2) == eS.takeRight(length / 2)
      } yield e
    println(rs.mkString(","))
    println(rs.sum)
  }

}