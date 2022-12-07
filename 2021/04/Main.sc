import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining._

val day = "04"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
val entries = srcs.map(_.getLines())

extension (self: Int) {
  def r = self / 5
  def c = self % 5
}

for {
  lines <- entries
} {
  val calls = lines.next().split(",").map(_.toInt)

  val boardMaps = lines
    .grouped(6)
    .map(_.drop(1))
    .map(_.flatMap(_.trim.split(" +").map(_.toInt)).zipWithIndex.toMap)
    .toSeq

  val foo = for {
    (call, ci) <- calls.zipWithIndex
    (boardMap, bi) <- boardMaps.zipWithIndex
    pos <- boardMap.get(call)
  } yield {
//    pos
    println((ci, call, bi, pos.c, pos.r))
    pos
  }
//  println(foo.groupBy(_.c))
  println
}