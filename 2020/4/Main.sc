import java.io.File
import scala.io.Source
import scala.util.Try



val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2020")
val input = new File(workingDir, "/4/input")
println(input.getAbsolutePath)

val entries: Array[Map[String, String]] = Source.fromFile(input)
  .mkString
  .split("\n\n")
  .map(_.split("""\s"""))
  .map(_
    .map(_.split(":"))
    .map{ case Array(k, v) => k -> v }
  )
  .map(_.toMap)

val required = Set(
  "byr", // (Birth Year)
  "iyr", // (Issue Year)
  "eyr", // (Expiration Year)
  "hgt", // (Height)
  "hcl", // (Hair Color)
  "ecl", // (Eye Color)
  "pid", // (Passport ID)
//  "cid", // (Country ID)
)

//Part 1
entries.map(_.keySet).count(required.subsetOf)

//Part 2
object IntE  { def unapply(arg: String): Option[Int]  = arg.toIntOption }

val validFns: Map[String, String => Boolean] = Map(
  "byr" -> (v => (1920 to 2002).contains(v.toInt)),
  "iyr" -> (v => (2010 to 2020).contains(v.toInt)),
  "eyr" -> (v => (2020 to 2030).contains(v.toInt)),
  "hgt" -> {
    case s"""${IntE(cm)}cm""" => (150 to 193).contains(cm)
    case s"""${IntE(in)}in""" => (59 to 76).contains(in)
    case _ => false
  },
  "hcl" -> """#[0-9a-f]{6}""".r.matches,
  "ecl" -> Set("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains,
  "pid" -> """[0-9]{9}""".r.matches,
)

entries.count { e =>
  e.forall { case (k, v) =>
    validFns.keySet.subsetOf(e.keySet) &&
    validFns.getOrElse(k, (_: String) => true)(v)
  }
}
