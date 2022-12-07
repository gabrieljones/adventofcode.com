import java.io.File

import scala.io.Source

val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val input = new File(workingDir, "/01/input")
def src = Source.fromFile(input)
def entries = src.getLines().map(BigInt.apply).toSeq

entries.sliding(2).count { case Seq(a, b) => a < b }

entries.sliding(3).map(_.sum).sliding(2).count { case Seq(a, b) => a < b }