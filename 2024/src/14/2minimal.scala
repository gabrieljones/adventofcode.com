package `14`

import java.io.{ByteArrayOutputStream, File}
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2minimal` extends App {

  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
//    "example",
//    "example0",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  val Pattern = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".r

  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val bots = src.getLines()
      .map { case Pattern(px, py, vx, vy) => (px.toInt, py.toInt, vx.toInt, vy.toInt) }
      .toArray

    val wide = bots.maxBy(_._1)._1 + 1
    val tall = bots.maxBy(_._2)._2 + 1

    (0 to 10000)
      .iterator
      .map { s =>
        val raster = new java.awt.image.BufferedImage(wide, tall, java.awt.image.BufferedImage.TYPE_USHORT_GRAY)
        bots
          .map { case (px, py, vx, vy) => (Math.floorMod(px + s * vx, wide), Math.floorMod(py + s * vy, tall)) }
          .foreach { case (px, py) => raster.setRGB(px, py, Int.MaxValue) }
        val os = new ByteArrayOutputStream(1200)
        javax.imageio.ImageIO.write(raster, "png", os)
        os.size() -> s
      }
      .min
      .tap(println)
  }
}
