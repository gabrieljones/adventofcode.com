package `01`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val Seq(a, b) =  src.getLines().map(_.split("   ").map(_.toInt)).toSeq.transpose
    val similarities = a.map(e => e * b.count(_ == e))
    val similarity = similarities.sum
    println(similarity)
  }
}