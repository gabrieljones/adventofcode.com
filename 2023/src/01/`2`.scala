package `01`

import java.io.File
import java.nio.file.Path
import scala.annotation.tailrec
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {
  val workingDir = Path.of(s"/Users/gjone12/Projects/adventofcode.com/2023/src/${this.getClass.getPackageName}")
  val inputs = Seq("example2", "input").map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  val digitMap = "|one|two|three|four|five|six|seven|eight|nine".split("\\|").zipWithIndex.tail.toMap
  //reluctant regex
  val Pattern = """(.*?)(one|two|three|four|five|six|seven|eight|nine)(.*)""".r

  @tailrec
  def deParse(str: String): String = str match {
    case Pattern(init, spelledOut, tail) =>
      deParse(s"$init${digitMap(spelledOut)}$tail")
    case str => str
  }
  
  def deParseOverlaps(str: String): String = {
    val Pattern = """^(one|two|three|four|five|six|seven|eight|nine).*""".r
    str match {
      case "" => ""
      case Pattern(spelledOut) =>
        s"${digitMap(spelledOut)}${deParseOverlaps(str.tail)}"
      case str => s"${str.head}${deParseOverlaps(str.tail)}"
    }
  }

  for {
    lines <- entries
  } {
    lines
      .map(_.tap(x => print(x + "\n" + deParse(x) + "\n" + deParseOverlaps(x) + "\n")))
      .map(deParseOverlaps)
      .map(_.filter(_.isDigit))
      .map(x => s"${x.head}${x.last}")
      .map(_.toLong)
      .map(_.tap(println))
      .sum
      .pipe(println)
  }
}