package `02`

import java.io.File
import java.nio.file.Path
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `2` extends App {
  val workingDir = Path.of(s"/Users/gjone12/Projects/adventofcode.com/2023/src/${this.getClass.getPackageName}")
  val inputs = Seq("example2", "input").map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

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
    .map(g => g.rs.map(_.r).max * g.rs.map(_.g).max * g.rs.map(_.b).max)
    .sum
    .tap(println)
}
