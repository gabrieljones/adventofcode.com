package `01`

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{FileSystems, Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {
  val cwd = Paths.get(".")
  println(cwd.toAbsolutePath)
  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  println(workingDir.toAbsolutePath)
  val inputs = Seq("example", "input").map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def entries = srcs.map(_.getLines())

  for {
    lines <- entries
  } {
    lines.map(_.filter(_.isDigit)).map(x => s"${x.head}${x.last}").map(_.toLong).sum
      .pipe(println)
  }
}