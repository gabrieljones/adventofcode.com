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
    src
      .getLines()
      .flatMap(Pattern.findAllIn(_))
      .foldLeft((true, 0L)) {
        case ((_   , acc), "do()")    => (true , acc)
        case ((_   , acc), "don't()") => (false, acc)
        case ((true, acc), MulPattern(a, b)) => (true, acc + a.toLong * b.toLong)
        case (z, _) => z //a mul after a don't, ignore it and pass z along
      }
      .pipe(_._2) //just the Long from the z tuple (Boolean, Long)
      .tap(println)
  }
}