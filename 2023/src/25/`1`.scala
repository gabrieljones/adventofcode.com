package `25`

import java.io.{BufferedWriter, File, FileWriter}
import java.lang.Math.log10
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
  for {
    lines <- entries
  } {
    val edges: Seq[(String, Seq[String])] = for {
      line <- lines.toSeq
      row = line.split(":? ")
      n: String = row.head
      e: String <- row.tail
    } yield {
      n -> Seq(e)
    }
    //convert to adjacency matrix
//    val nodes = edges.flatMap(_._1 +: _._2).distinct.sorted


    
    
    
    
  }

//  for {
//    (lines, i) <- entries.zipWithIndex
//  } {
//    println("graph TD")
//    (for {
//      line <- lines
//      row = line.split(":? ")
//      n: String = row.head
//      e: String <- row.tail
//    } yield {
//      s"$n --- $e"
//    })
//      .mkString("graph TD\n  ","\n  ", "")
//      .pipe { s =>
//        new File(s"o$i.txt").pipe(new FileWriter(_)).pipe(new BufferedWriter(_))
//          .tap(_.write(s))
//          .close()
//      }
//  }
}
