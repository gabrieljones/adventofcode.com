import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "11"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

val adjM = for {
  r <- Seq(-1, 0, 1)
  c <- Seq(-1, 0, 1)
  if (r, c) != (0, 0)
} yield (r, c)

for {
  lines <- entries
} {
  val octogrid = lines.flatMap(_.map(_.asDigit)).toArray
  var next = octogrid.map(identity)
  var flashes = 0
  for {
    step <- 1 to 100
  } {
//    if (step % 10 == 0) {
//      println(s"$step")
//      println(next.toSeq.grouped(10).map(_.mkString).mkString("\n"))
//    }
    next = next.map(_ + 1)
    for {
      _ <- 0 until 20
      (v, i) <- next.zipWithIndex
      //    _ = println((v, i))
      if v > 9
      _ = flashes += 1
      r = i / 10
      c = i % 10
//      _ = println((v, i, r * 10 + c))
      _ = next(r * 10 + c) = 0
      (ra, ca) <- adjM.map(o => (r + o._1, c + o._2))
      if (0 until 10).contains(ra)
      if (0 until 10).contains(ca)
//      _ = println((ra, ca))
      if next(ra * 10 + ca) > 0
    } {
      next(ra * 10 + ca) += 1
    }
  }
  println(s"=$flashes")
}


for {
  lines <- entries
} {
  val octogrid = lines.flatMap(_.map(_.asDigit)).toArray
  var next = octogrid.map(identity)
  for {
    step <- 1 to 600
  } {
//    if (step % 10 == 0) {
//      println(s"$step")
//      println(next.toSeq.grouped(10).map(_.mkString).mkString("\n"))
//    }
    next = next.map(_ + 1)
    var flashes = 0
    for {
      _ <- 0 until 20
      (v, i) <- next.zipWithIndex
      if v > 9
      _ = flashes += 1
      r = i / 10
      c = i % 10
      _ = next(r * 10 + c) = 0
      (ra, ca) <- adjM.map(o => (r + o._1, c + o._2))
      if (0 until 10).contains(ra)
      if (0 until 10).contains(ca)
      if next(ra * 10 + ca) > 0
    } {
      next(ra * 10 + ca) += 1
    }
    if (flashes > 50)
    println(s"$step=$flashes")

  }
}