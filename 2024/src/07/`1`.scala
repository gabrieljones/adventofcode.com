package `07`

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  val Pattern = """(\d+): (.+)""".r
  for (src <- srcs) {
    var result = 0L
    for {
      line <- src.getLines()
    } {
      line match {
        case Pattern(testStr, nsStr) =>
          val test = testStr.toLong
          val ns = nsStr.split(" ").map(_.toLong).toSeq
          val oQ = 1 << (ns.length)
          val opsI = ((1 << (ns.length - 1)) until (1 << ns.length)).iterator
          var att = 0L
          while (att != test && opsI.hasNext) {
            val ops = opsI.next
            att = ops.toBinaryString.drop(1).zip(ns.tail)
              .foldLeft(ns.head)((acc, op) => op match {
                case ('0', n) => acc + n
                case ('1', n) => acc * n
              })
          }
          if (att == test) {
            result += att
          }
      }
    }
    println(result)
  }
}
