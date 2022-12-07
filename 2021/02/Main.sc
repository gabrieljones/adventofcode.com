import java.io.File

import scala.io.Source

val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val input = new File(workingDir, "/02/input")
def src = Source.fromFile(input)
def entries = src.getLines().map(_.split(" ")).map(s => s(0) -> s(1).toLong)

{
  var h, d = 0.toLong

  for {
    (dir, c) <- entries
  } {
    dir match {
      case "forward" => h += c
      case "up" => d -= c
      case "down" => d += c
    }
  }

  println(h)
  println(d)
  h * d
}

{
  var h, d, a = 0.toLong

  for {
    (dir, c) <- entries
  } {
    dir match {
      case "forward" => h += c; d += (c * a)
      case "up" => a -= c
      case "down" => a += c
    }
//    println((a, dir, c, h))
  }

  println(h)
  println(d)
  h * d
}