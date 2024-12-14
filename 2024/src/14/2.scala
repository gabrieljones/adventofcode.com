package `14`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.BoldMode
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalSize, TextColor}

import java.awt.Font
import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `2` extends App {

  val fonts: Array[Font] = Array(new Font("MesloLGS NF", Font.PLAIN, 10)) //effectively ignores all the code above this line
  val defaultTerminalFactory = new DefaultTerminalFactory()
  val fontConfig = new SquareFontConfig(false, BoldMode.EVERYTHING_BUT_SYMBOLS, fonts, Option(10))
  val fontConfiguration: SwingTerminalFontConfiguration = fontConfig
  defaultTerminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration)
  defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(103, 103))

  val terminal = defaultTerminalFactory.createTerminal()
  terminal.enterPrivateMode()
  val g = terminal.newTextGraphics()
  g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT)

  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
//    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  val Pattern = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".r
  def srcs = inputs.map(Source.fromFile)

  for (src <- srcs) {
    val bots = src.getLines()
      .map {
        case Pattern(px, py, vx, vy) => ((px.toInt, py.toInt), (vx.toInt, vy.toInt))
      }
      .toSeq

    val wide  = bots.maxBy(_._1._1)._1._1 + 1
    val wideM = wide / 2
    val tall  = bots.maxBy(_._1._2)._1._2 + 1
    val tallM = tall / 2

    bots.foreach { case ((px, py), (vx, vy)) =>
      var cnc = g.getCharacter(px, py).getCharacter.asDigit
      if (cnc == -1) cnc = 0
      val cnn = cnc + 1
      g.putString(px, py, cnn.toString)
      terminal.flush()
    }
/*
    for (s <- 0 to 20000) {
      val finalPos = bots.map { case ((px, py), (vx, vy)) => (Math.floorMod(px + s * vx, wide), Math.floorMod(py + s * vy, tall)) }.toSeq

      //create png raster
      val raster = new java.awt.image.BufferedImage(wide, tall, java.awt.image.BufferedImage.TYPE_USHORT_GRAY)
      finalPos
        .foreach { case (px, py) =>
          raster.setRGB(px, py, Int.MaxValue)
        }
      val file = new File(f"src/14/img/$s%06d.png")

      javax.imageio.ImageIO.write(raster, "png", file)

    }

    System.exit(0)
*/



    var s = 7572
    var running = true
    while (running) {
      if (s> 10000) running == false
      val input = terminal.pollInput()
      input match {
        case null =>
        case c if c.getCharacter == 'j' => s -= 1
        case c if c.getCharacter == 'k' => s += 1
        case c if c.getCharacter == 'q' => running = false
      }


      val finalPos = bots.map { case ((px, py), (vx, vy)) => (Math.floorMod(px + s * vx, wide), Math.floorMod(py + s * vy, tall)) }.toSeq

      terminal.clearScreen()
      finalPos
//        .filterNot((x, y) => x == wideM || y == tallM)
        .foreach { case (px, py) =>
          var cnc = g.getCharacter(px, py).getCharacter.asDigit
          if (cnc == -1) cnc = 0
          val cnn = cnc + 1
          g.putString(px, py, cnn.toString)
        }
      g.putString(0, 0, f"$s%020d")
      terminal.flush()
      Thread.sleep(10)

    }
    terminal.close()
  }
}
