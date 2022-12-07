import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "07"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

for {
  lines <- entries
} {
  val poss = lines.next().split(",").map(_.toInt)
  val fuelMap = for {
    p <- poss.min to poss.max
  } yield {
    val sum = poss
      .map(_ - p)
      .map(Math.abs)
//      .map(BigInt.apply)
//      .map(_.tap(i => print(f"$i%5d")))
      .sum
//    println(f"$sum%7d")
    p -> sum
  }
  println(fuelMap.minBy(_._2))
//  println(fuelMap.mkString("\n"))

}

for {
  lines <- entries
} {
  val poss = lines.next().split(",").map(_.toInt)
  val fuelMap = for {
    p <- poss.min to poss.max
  } yield {
    p -> poss
      .map(_ - p)
      .map(Math.abs)
      .map(n => n * (n+1)/2)
      .sum
  }
  println(fuelMap.minBy(_._2))

}