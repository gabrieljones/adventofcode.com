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

  val Pattern = """do\(\)|don't\(\)|mul\(\d+,\d+\)""".r
  val MulPattern = """mul\((\d+),(\d+)\)""".r

  for (src <- srcs) {
    var enabled = true
    var sum: Long = 0
    src
      .getLines()
      .flatMap(Pattern.findAllIn(_))
      .foreach {
        case "do()"    => enabled = true
        case "don't()" => enabled = false
        case MulPattern(a, b) if enabled =>
          sum += a.toInt * b.toInt
        case _ => // ignore
      }
    println(sum)
  }
}