import java.io.File

import scala.io.Source

val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2020")
val input = new File(workingDir, "/2/input")
println(input.getAbsolutePath)
def src = Source.fromFile(input)
val Pattern = """(\d+)-(\d+) (\w): (\w+)""".r
object IntE  { def unapply(arg: String): Option[Int]  = arg.toIntOption }
object CharE { def unapply(arg: String): Option[Char] = arg.headOption }
def entries = for {
  Pattern(IntE(min), IntE(max), CharE(c), p) <- src.getLines()
} yield {
  (min, max, c, p)
}

val result = entries.withFilter{ case (min, max, c, p) =>
//  val count = p.count(_ == c)
//  count >= min && count <= max
  min to max contains p.count(_ == c)
}
result.size

val result2 = entries.withFilter{ case (i0, i1, c, p) =>
//  p(i0-1) == c ^ p(i1-1) == c
  Seq(i0, i1).map(_ - 1).map(p).count(_ == c) == 1
}
result2.size

src.close()
