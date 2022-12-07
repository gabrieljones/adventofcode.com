import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "06"
println(System.getProperties.toString.split(",").mkString("\n"))
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

def dec(i: Int) =  if (i == 0) 6 else i - 1

for {
  lines <- entries
} {
  var ages = lines.next().split(",").map(_.toInt)
//  println(ages.mkString("Array(", ", ", ")"))
  for {
    d <- 1 to 80
  } {
    val newFishCount = ages.count(_ == 0)
    ages = ages.map(dec) ++ Array.fill(newFishCount)(8)
//    println(ages.mkString("Array(", ", ", ")"))
  }
  println(ages.length)
}

def dec2(i: (Int, BigInt)): (Int, BigInt) = dec(i._1) -> i._2

for {
  lines <- entries
} {
  var ages: Seq[(Int, BigInt)] = lines.next().split(",").toSeq.map(_.toInt -> BigInt(1))
  //  println(ages.mkString("Array(", ", ", ")"))
  for {
    d <- 1 to 256
  } {
    val agesMap = ages.groupBy(_._1)
      .map { case (k, vs) => k -> vs.map(_._2).sum }
    val newFishCount = agesMap.getOrElse(0, BigInt(0))
    ages = agesMap.toSeq.map(dec2) :+ 8 -> newFishCount
    //    println(ages.mkString("Array(", ", ", ")"))
  }
  println(ages)
  println(ages.map(_._2).sum)
}
