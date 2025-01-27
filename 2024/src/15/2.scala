package `15`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

import lib.*

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example2small",
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val moveTable = Map(
    '<' -> (-1, 0),
    '>' -> ( 1, 0),
    '^' -> ( 0,-1),
    'v' -> ( 0, 1),
  )

  for (src <- srcs) {
    val lines = src.getLines()
    val map = lines.takeWhile(_ != "").map(_.flatMap{
      case '#' => "##"
      case 'O' => "[]"
      case '.' => ".."
      case '@' => "@."
    }.toArray).toArray

    val moves = lines.mkString

    println(s"map:\n${map.map(_.mkString).mkString("\n")}")
    println(s"moves: $moves")

    val tall = map.length
    val wide  = map(0).length

    var xy = (0,0)

    for {
      yi <- 0 until tall
      xi <- 0 until wide
    } {
      if (map(xi,yi) == '@') {
        xy = (xi,yi)
      }
    }

    for {
      m <- moves
      d = moveTable(m)
    } {
      map(xy + d) match {
        case '#' => //do nothing
        case '.' =>
          map(xy) = '.'
          map(xy + d) = '@'
          xy = xy + d
        case '[' =>
          var bxy = xy + d
          while (map(bxy) == '[') {
            bxy = bxy + d
          }
          if (map(bxy) == '.') {
            map(xy) = '.'
            map(xy + d) = '@'
            map(bxy) = '['
            xy = xy + d
          }
        case ']' =>
          var bxy = xy + d
          while (map(bxy) == ']') {
            bxy = bxy + d
          }
          if (map(bxy) == '.') {
            map(xy) = '.'
            map(xy + d) = '@'
            map(bxy) = ']'
            xy = xy + d
          }
      }
    }
    println(s"map:\n${map.map(_.mkString).mkString("\n")}")

    val coords = (0 until tall).iterator.flatMap(yi => (0 until wide).iterator.map(xi => xi -> yi))

    coords.collect {
      case c if map(c) == 'O' => c.x * 100 + c.y
    }
      .sum
      .tap(println)
  }
}
