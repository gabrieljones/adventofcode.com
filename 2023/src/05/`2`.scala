package `05`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.immutable.NumericRange
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

object `2` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  val Triple = """(\d+) (\d+) (\d+)""".r

  type R = NumericRange[Long]
  type M = (Long, Long, Int)
  extension (maps: Seq[M]) {
    def mapping(i: Seq[R]): Seq[R] = {
      maps.foldLeft(i) {
        case (r, o) => r
        case (acc, _) => acc
      }
    }
    def inverse(i: Long): Long = {
      val result = maps.foldLeft(-1L) {
        case (-1, (b, a, c)) if i >= b && i < b + c =>
          i - b + a
        case (acc, _) => acc
      }
      if (result == -1) i else result
    }  }

  for {
    lines: Iterator[String] <- entries.map(_ ++ Iterator("", "")) //append two empty lines so .takeWhile(_ => lines.hasNext) will work
  } {
    val seeds: Seq[R] = lines.next()
      .pipe(_.split(" ").drop(1))
      .toSeq
      .map(_.toLong)
      .grouped(2)
      .map { case Seq(s, l) => s to s + l - 1 }
      .toSeq

    lines.next()

    val maps: Iterator[Seq[M]] = Iterator.continually {
        lines.next()
        lines.takeWhile(_.nonEmpty)
          .map {
            _.split(" ")
          }
          .map { case Array(so, si, l) => (so.toLong, si.toLong, l.toInt) }
          .toSeq
      }
      .takeWhile(_ => lines.hasNext)

    maps
      .toSeq
      .tap(println)

    val reverseMaps: Seq[Seq[M]] = maps.toSeq.reverse

    var loc = 1L
    while {
      val seed = reverseMaps.foldLeft(loc
//          .tap(x => print(s"$x > "))
        ) {
          case (acc, map) => map.inverse(acc)
            .tap(x => print(s"$x > "))
        }
//        .tap(_ => println())
      !seeds.forall(_.contains(seed))
    } do {
      loc += 1
    }

    println(loc)
  }


//  locs
//    .tap(println)
//    .min
//    .tap(println)

}
