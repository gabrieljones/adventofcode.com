package `02`

import java.io.File
import java.nio.file.Path
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
  val workingDir = Path.of(s"/Users/gjone12/Projects/adventofcode.com/2023/src/${this.getClass.getPackageName}")
  val inputs = Seq("example", "input").map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  case class Round(r: Int, g: Int, b: Int)

  case class Game(n: Int, rs: Seq[Round])

  val Pattern = """Game (\d+): (.+)""".r
  val Color = """(\d+) (.+)""".r
  for {
    lines <- entries
  } {
    lines.map {
      case Pattern(n, roundsStr) =>
        val rounds = roundsStr.split("; ").map { roundStr =>
          val m = roundStr.split(", ")
            .map { case Color(n, c) => c -> n.toInt }.toMap
            .withDefault(_ => 0)
          Round(m("red"), m("green"), m("blue"))
        }
          .toSeq
        Game(n.toInt, rounds)
    }
  }
//    .toSeq
    .filterNot(_.rs.exists(r => r.r > 12 || r.g > 13 || r.b > 14))
//    .tap(_.foreach(println))
    .map(_.n)
//    .tap(_.foreach(println))
    .sum
    .tap(println)
}
