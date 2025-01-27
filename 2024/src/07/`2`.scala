package `07`

import lib.*

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {
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
    src.getLines().foreach { case Pattern(testStr, nsStr) =>
      val test = testStr.toLong
      val ns   = nsStr.split(" ").map(_.toLong).toSeq
      val opsR = ((3 ** (ns.length - 1)) until (3 ** (ns.length)))
//      println(opsR.toString() + " :: " + ns.length)
      //base 3 numbers to generate all permutations
      //100, 101, 102, 110, 111, 112, 120, 121, 122
      //with (radix 3 string).drop(1) will become
      // 00,  01,  02,  10,  11,  12,  20,  21,  22
      val opsI = opsR.iterator
      var att  = 0L
      while (att != test && opsI.hasNext) {
        val ops = opsI.next
//        println(s"$ops:${Integer.toString(ops, 3)}")
        att = Integer.toString(ops, 3).drop(1).zip(ns.tail)
//          .tap(println)
          .foldLeft(ns.head)((acc, op) => op match {
            case ('0', n) => acc + n
            case ('1', n) => acc * n
            case ('2', n) => s"$acc$n".toLong
          })
      }
      if (att == test) {
        result += att
      }
    }
    println(result)
    //    println("^^^^")
  }
}
