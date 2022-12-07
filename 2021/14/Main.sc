import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "14"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

val Pattern = """([A-Z]{2}) -> ([A-Z])""".r

for {
  lines <- entries
} {
  var poly = lines.next()
  lines.next()
  val rules = lines.toSeq.map { case Pattern(k, v) => k -> v }.toMap
  println(rules)

  for {
    step <- 1 to 10
  } {
//    println(poly)
    poly = (poly.head :: poly.sliding(2).flatMap(e => rules(e) + e(1)).toList).mkString
  }
  val counts = poly.groupBy(identity).view.mapValues(_.length)
  println(counts.toMap)
  val min = counts.minBy(_._2)
  val max = counts.maxBy(_._2)
  println((max, min))
  println(max._2 - min._2)
}

for {
  lines <- entries
} {
  var poly = lines.next()
  lines.next()
  val rules = lines.toSeq.map { case Pattern(k, v) => k -> v }.toMap
  println(rules)

  for {
    step <- 1 to 11
  } {
    //    println(poly)
    poly = (poly.head :: poly.sliding(2).flatMap(e => rules(e) + e(1)).toList).mkString
  }
  val counts = poly.groupBy(identity).view.mapValues(_.length)
  println(counts.toMap)
  val min = counts.minBy(_._2)
  val max = counts.maxBy(_._2)
  println((max, min))
  println(max._2 - min._2)
}
