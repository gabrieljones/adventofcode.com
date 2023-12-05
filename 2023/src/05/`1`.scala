package `05`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  val Triple = """(\d+) (\d+) (\d+)""".r

  extension (maps: Seq[Seq[Long]]) {
    def mapping(i: Long): Long =
      val result = maps.foldLeft(-1L) {
        case (-1, Seq(a, b, c)) if i >= b && i < b + c =>
           i - b + a
        case (acc, _) => acc
      }
      if (result == -1) i else result
  }

  for {
    lines: Iterator[String] <- entries.map(_ ++ Iterator("", ""))
  } {
    val seeds = lines.next()
      .pipe(_.split(" ").drop(1))
      .map(_.toLong)
      .toSeq
    lines.next()
    val maps: Seq[Seq[Seq[Long]]] = Iterator.continually {
      lines.next()
      lines.takeWhile(_.nonEmpty)
        .map { _.split(" ").map(_.toLong).toSeq }
        .toSeq
    }
      .takeWhile(_ => lines.hasNext)
      .toSeq

    maps
      .tap(println)

    val locs = seeds.map{ s =>
      maps.foldLeft(s.tap(x => print(s"$x > "))) {
        case (acc, map) => map.mapping(acc).tap(x => print(s"$x > "))
      }
        .tap(_ => println())
    }

    locs
      .tap(println)
      .min
      .tap(println)
  }
}