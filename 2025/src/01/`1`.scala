package `01`

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

  val RegEx = """([RL])(\d+)""".r

  val dirMap = Map(
    "R" -> 1,
    "L" -> -1,
  )

  for (src <- srcs) {
    val rs =  src.getLines().foldLeft(Seq(50)) {
      case (z@ _ :+ last, RegEx(dir, dist)) =>
        val dirI = dirMap(dir)
        val next: Int = (last + (dirI * dist.toInt) + 100) % 100
        z :+ next
    }
    println(rs.mkString(","))
    println(rs.count(_ == 0))
  }

}