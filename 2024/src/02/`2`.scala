package `02`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source

object `2` extends App {
  println("02")
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def diffs(arr: Array[Int]) = arr.sliding(2).map{ case Array(a, b) => a - b }.toArray

  def upDown(arr: Array[Int]) = arr.map(_ > 0).toSet.size > 1

  def stepTooBig(arr: Array[Int]) = arr.map(Math.abs).exists(_ > 3)

  def _isSafe(arr: Array[Int]) = {
    val diffs_ = diffs(arr)
    val noStep = diffs_.contains(0)
    !upDown(diffs_) && !stepTooBig(diffs_) && !noStep
  }

  def isSafe(arr: Array[Int]): Boolean = {
    if (_isSafe(arr)) {
      true
    } else {
      //loop arr
      val n = arr.length
      for (i <- 0 until n) {
        //copy array without ith element
        val arr_ = arr.slice(0, i) ++ arr.slice(i + 1, n)
        if (_isSafe(arr_)) {
          return true
        }
      }
      false
    }
  }

  for (src <- srcs) {
    val reports: Iterator[Array[Int]] =  src.getLines().map(_.split(" ").map(_.toInt))

    val result = reports.count(isSafe)
    println(result)
  }
}