package AdventOfCode

import scala.io.Source

object Day08 extends App {
  val W = 25
  val H = 6

  private val source = Source.fromFile("src/main/scala/AdventOfCode/Day08.input")
  val sif = source.mkString.map(_.asDigit).toArray
  source.close()

  val layers: Array[Array[Int]] = sif.grouped(W * H).toArray
  val withMinZ = layers.minBy(_.count(_ == 0))
  println(withMinZ.grouped(W).map(_.mkString).mkString("\n"))
  val grouped = withMinZ.groupBy(identity)
  val cs = grouped.view.mapValues(_.length)
  println(cs(1) * cs(2))

  val pixels: Array[Array[Int]] = layers.transpose
  val image: Array[Int] = pixels.map(_.foldLeft(2)((z, e)=> if (z == 2) e else z))

  val colors = Map(0 -> ' ', 1 -> '█')
  println(image.map(colors).grouped(W).map(_.mkString).mkString("\n"))
}
