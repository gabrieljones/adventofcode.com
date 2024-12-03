package `01`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val Seq(a, b) =  src.getLines().map(_.split(" {3}").map(_.toInt)).toSeq.transpose
    val pairs = a.sorted.zip(b.sorted)
    val diffs = pairs.map{ case (ae, be) => be - ae}.map(Math.abs)
    val sum = diffs.sum
    println(sum)
  }
}