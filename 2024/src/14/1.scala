package `14`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.{BoldMode, filterMonospaced}
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalSize, TextColor}

import java.awt.{Font, GraphicsEnvironment}
import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {

  val fonts: Array[Font] = Array(new Font("MesloLGS NF", Font.PLAIN, 20)) //effectively ignores all the code above this line
  val defaultTerminalFactory = new DefaultTerminalFactory()
  val fontConfig = new SquareFontConfig(false, BoldMode.EVERYTHING_BUT_SYMBOLS, fonts, Option(20))
  val fontConfiguration: SwingTerminalFontConfiguration = fontConfig
  defaultTerminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration)
  defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(20, 20))

  val terminal = defaultTerminalFactory.createTerminal()
  terminal.enterPrivateMode()
  val g = terminal.newTextGraphics()
  g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT)

  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  val Pattern = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".r
  def srcs = inputs.map(Source.fromFile)


  val seconds = 100

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

    bots.foreach{ case ((px, py), (vx, vy)) =>
      var cnc = g.getCharacter(px, py).getCharacter.asDigit
      if (cnc == -1) cnc = 0
      val cnn = cnc + 1
      g.putString(px, py, cnn.toString)
      terminal.flush()
    }


    val finalPos = bots.map { case ((px, py), (vx, vy)) => (Math.floorMod(px + 100*vx, wide), Math.floorMod(py + 100*vy, tall)) }.toSeq

    terminal.clearScreen()
    finalPos
      .filterNot((x, y) => x == wideM || y == tallM)
      .foreach { case (px, py) =>
        var cnc = g.getCharacter(px, py).getCharacter.asDigit
        if (cnc == -1) cnc = 0
        val cnn = cnc + 1
        g.putString(px, py, cnn.toString)
        terminal.flush()
      }
    finalPos
      .filterNot((x, y) => x == wideM || y == tallM)
      .groupBy((x,y)=>(x/(wideM+1), y/(tallM+1)))
      .tap(println)
      .map(_._2.size)
      .product
      .tap(println)
  }
}