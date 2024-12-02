package `02`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source

object `1` extends App {
  println("02")
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val reports: Iterator[Array[Int]] =  src.getLines().map(_.split(" ").map(_.toInt))
    val results = reports.map { r =>
      val diffs = r.sliding(2).map{ case Array(a, b) => a - b }.toSeq
      val dirs = diffs.map(_ > 0)
      val diffDir  = dirs.toSet.size > 1
      val stepTooBig = diffs.map(Math.abs).exists(_ > 3)
      val noStep = diffs.contains(0)
      if (diffDir || stepTooBig || noStep) {
        "Unsafe"
      } else {
        "Safe"
      }
    }.toSeq
    val result = results.count(_ == "Safe")
    println(result)
  }
}