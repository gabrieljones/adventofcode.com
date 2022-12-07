import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "05"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

val Pattern = """(\d+),(\d+) -> (\d+),(\d+)""".r

for {
  lines <- entries
} {
  var map = Map.empty[(Int, Int), Int].withDefaultValue(0)
  for {
    Pattern(x1S, y1S, x2S, y2S) <- lines
    x1 = x1S.toInt
    y1 = y1S.toInt
    x2 = x2S.toInt
    y2 = y2S.toInt
    if x1 == x2 || y1 == y2
    x <- Math.min(x1,x2) to Math.max(x1,x2)
    y <- Math.min(y1,y2) to Math.max(y1,y2)
  } {
    map = map.updated((x,y), (map((x,y)) + 1))
  }
  println(map.count((_, oc)=>oc > 1))
}

def signum(i: Int) = if (i < 0) -1 else if (i > 0) 1 else 0

extension (self: (Int, Int)) {
  def to(that: (Int, Int)): Seq[(Int, Int)] = {
    val inc = (signum(that._1 - self._1), signum(that._2 - self._2))
    Iterator.from(0)
      .map(i => (self._1 + i * inc._1, self._2 + i * inc._2))
      .takeWhile(_!=that)
      .toSeq
      :+ that
  }
}

for {
  lines <- entries
} {
  var map = Map.empty[(Int, Int), Int].withDefaultValue(0)
  for {
    Pattern(x1S, y1S, x2S, y2S) <- lines
    x1 = x1S.toInt
    y1 = y1S.toInt
    x2 = x2S.toInt
    y2 = y2S.toInt
    (x, y) <- (x1, y1) to (x2, y2)
  } {
    map = map.updated((x,y), (map((x,y)) + 1))
  }
  println(map.count((_, oc)=>oc > 1))
}