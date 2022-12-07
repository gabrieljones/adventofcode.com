import java.io.File

import scala.io.Source

val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2020")
val input = new File(workingDir, "/1/input")
println(input.getAbsolutePath)
def src = Source.fromFile(input)
def entries = src.getLines().map(BigInt.apply).toSeq

val result = entries.combinations(2)
  .withFilter(_.sum == 2020)
  .map(c => c -> c.product)
result.toSeq

val result2 = entries.combinations(3)
  .withFilter(_.sum == 2020)
  .map(c => c -> c.product)
result2.toSeq
src.close()
