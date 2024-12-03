package `03`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example2",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val pattern = """do\(\)|don't\(\)|mul\(\d+,\d+\)""".r

  for (src <- srcs) {
    val lines    = src.getLines()
    val matches = lines.flatMap(pattern.findAllIn(_))

    var enabled = true
    var sum: Long = 0
    while (matches.hasNext) {
      val m = matches.next()
      print(m)
      if (m == "do()") {
        enabled = true
      } else if (m == "don't()") {
        enabled = false
      } else if (enabled) {
        val args   = m.drop(4).dropRight(1).split(",").map(_.toLong)
        print(" = ")
        val product = args.product
        print(product)
        sum += product
      }
      println()
    }

    println(sum)
  }
}