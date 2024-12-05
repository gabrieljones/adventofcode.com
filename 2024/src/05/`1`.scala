package `05`

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
    val li = src.getLines()
    val rules  : Array[(Int, Int)] = li.takeWhile(_ != "").map(_.split("\\|").map(_.toInt).pipe { case Array(a, b) => a -> b } ).toArray
    val updates: Array[Array[Int]] = li.map(_.split(",").map(_.toInt)).toArray
    rules.foreach(println)
    updates.map(_.mkString(",")).foreach(println)
    updates.flatMap { u =>
      var correct = true
      val ri = rules.iterator
      while (correct && ri.hasNext) {
        val (a, b) = ri.next()
        val ai = u.indexOf(a)
        val bi = u.indexOf(b)
        correct = ai == -1 || bi == -1 || ai < bi
      }
      if (correct) Some(u) else None
    }
      .tap(_ => println())
      .tap(_.map(_.mkString(",")).foreach(println))
      .map(u => u(u.length/2))
      .tap(_.foreach(println))
      .sum
      .tap(println)
  }
}