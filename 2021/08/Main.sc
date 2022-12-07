import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining.*

val day = "08"
val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val inputs = Seq("example","input").map(p => new File(workingDir, s"/$day/$p"))
def srcs = inputs.map(Source.fromFile)
def entries = srcs.map(_.getLines())

val unos = Set(2,4,3,7)
for {
  lines <- entries
} {
  val ds = for {
    line <- lines
    Array(usp, fdo) = line.split(" \\| ")
    d <- fdo.split(" ")
  } yield d
  println(ds.map(_.length).count(unos.contains))
}

for {
  lines <- entries
} {
  val ds = for {
    line <- lines
    Array(usp, fdo) = line.split(" \\| ")
    d <- fdo.split(" ")
  } yield d
  println(ds.map(_.length).count(unos.contains))
}

