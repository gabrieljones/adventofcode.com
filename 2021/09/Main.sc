import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "09"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

val adjM = for {
  r <- Seq(-1, 0, 1)
  c <- Seq(-1, 0, 1)
  if Math.abs(r + c) == 1
} yield (r, c)

for {
  lines <- entries
} {
  val heightmap = lines.map(_.map(_.asDigit).toArray).toArray
  val iMap = (for {
    (row, r) <- heightmap.zipWithIndex
    (v, c) <- row.zipWithIndex
  } yield {
    (r,c) -> v
  })
    .toMap.withDefaultValue(Int.MaxValue)
  val risks = for {
    r <- 0 to heightmap.length
    c <- 0 to heightmap.head.length
    v = iMap((r, c))
    adjacentC = adjM.map(e => (r + e._1, c + e._2))
    adjacentV = adjacentC.map(iMap)
//    _ = println((adjacentC, v, r, c))
    if adjacentV.forall(_ > v)
  } yield v + 1
  println(risks)
  println(risks.sum)
}