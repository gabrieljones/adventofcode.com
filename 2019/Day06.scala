import java.io.File
import scala.io.Source

object Day06 extends App {
  println(new File(".").getAbsolutePath)

  val orbitMaps = Array(
    """COM)B
      |B)C
      |C)D
      |D)E
      |E)F
      |B)G
      |G)H
      |D)I
      |E)J
      |J)K
      |K)L""".stripMargin,
    """COM)B
      |B)C
      |C)D
      |D)E
      |E)F
      |B)G
      |G)H
      |D)I
      |E)J
      |J)K
      |K)L
      |K)YOU
      |I)SAN""".stripMargin,
    Source.fromFile("src/main/scala/AdventOfCode/Day06.input").mkString
  )
    .map(_.split("""\n""")
      .map(_.split(" //").head)
    )

  def countOrbits(m: Array[String]): Int = {
    val cs = Array.fill(m.length)(-1)
    val ots = m.map(_.split("\\)"))
    val is = ots.map(_ (1)).zipWithIndex.toMap
    var done = false
    while (!done) {
      done = true
      for {
        ori <- 0 until m.length
        Array(ee, _) = m(ori).split("\\)")
        eei = is.get(ee)
      } {
        eei.fold(cs(ori) = 1) {
          eei =>
            if (cs(eei) != -1) { // if orbitee count is found
              cs(ori) = cs(eei) + 1 // set orbitor count
            } else {
              done = false // orbitee count is not yet found, keep looping
            }
        }
      }
      println(cs.mkString(","))
    }
    cs.sum
  }

  for {
    orbitMap <- orbitMaps
    count = countOrbits(orbitMap)
  } {
    println(count)
  }

  def countTransfers(m: Array[String], begS: String, endS: String): Int = {
    val ots = m.map(_.split("\\)"))
    val is = ots.map(_ (1)).zipWithIndex.toMap
    val beg = is(begS)
    val end = is(endS)

    def path(o: String): List[String] = {
      is.get(o).fold(o :: Nil)(i => ots(i)(1) :: path(ots(i)(0)))
    }

    val begPath = path(begS)
    val endPath = path(endS)
    val diff = begPath.diff(endPath)
    val symDiff = begPath.diff(endPath).concat(endPath.diff(begPath))
    val intersect = begPath.intersect(endPath).reverse
    println(begPath.reverse)
    println(endPath.reverse)
    println(diff.reverse)
    println(symDiff.reverse)
    println(intersect.reverse)
    println(begPath.length + endPath.length - intersect.length * 2)
    symDiff.length - 2
  }

  for {
    orbitMap <- orbitMaps.drop(1)
    count = countTransfers(orbitMap, "YOU", "SAN")
  } {
    println(count)
  }
}