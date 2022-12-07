import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.{Failure, Success, Try}
import scala.util.chaining.*

val day = "10"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

val pairs = Map(
  '(' -> ')',
  '[' -> ']',
  '{' -> '}',
  '<' -> '>',
).withDefaultValue(Char.MaxValue)

case class Unexpected(c: Char) extends Exception(s"$c")

for {
  lines <- entries
} {
  val scores = for {
    line <- lines
  } yield {
    Try(line.foldLeft(List.empty[Char]){
      case (z, c) if pairs.keySet.contains(c) => z :+ c
      case (z, c) if pairs.values.toSet.contains(c) && pairs(z.last) == c => z.init
      case (z, c) => throw Unexpected(c)
    }) match {
      case Success(value) => 0
      case Failure(Unexpected(')')) => 3
      case Failure(Unexpected(']')) => 57
      case Failure(Unexpected('}')) => 1197
      case Failure(Unexpected('>')) => 25137
    }
  }
  println(scores.sum)
}

val scoreMap = (' ' +: pairs.keys.toSeq).zipWithIndex.toMap

for {
  lines <- entries
} {
  val scores = (for {
    line <- lines
    res <- Try(line.foldLeft(List.empty[Char]){
      case (z, c) if pairs.keySet.contains(c) => z :+ c
      case (z, c) if pairs.values.toSet.contains(c) && pairs(z.last) == c => z.init
      case (z, c) => throw Unexpected(c)
    }).toOption
  } yield {
    res
      .reverse
      .map(scoreMap)
      .foldLeft(BigInt(0)){ case (z, n) => z * 5 + n }
  })
    .toSeq
    .sorted
  println(scores(scores.size/2))
}
