package `18`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.{BoldMode, filterMonospaced}
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalPosition, TerminalSize, TextColor}

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*
import lib.*

import java.awt.{Font, GraphicsEnvironment}
import scala.collection.mutable


object `1` extends App {

  val fonts: Array[Font] = Array(new Font("MesloLGS NF", Font.PLAIN, 10))
  val defaultTerminalFactory = new DefaultTerminalFactory()
  val fontConfig = new SquareFontConfig(false, BoldMode.EVERYTHING_BUT_SYMBOLS, fonts, Option(8))
  val fontConfiguration: SwingTerminalFontConfiguration = fontConfig
  defaultTerminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration)
  defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(200, 200))

  val terminal = defaultTerminalFactory.createTerminal()
  terminal.enterPrivateMode()
  val g = terminal.newTextGraphics()
  g.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT)

  enum Heading(val d: (Int, Int)) {
    case `↑` extends Heading((0, -1))
    case `→` extends Heading((1, 0))
    case `↓` extends Heading((0, 1))
    case `←` extends Heading((-1, 0))

    def left: Heading = this match {
      case `↑` => `←`
      case `←` => `↓`
      case `↓` => `→`
      case `→` => `↑`
    }

    def right: Heading = this match {
      case `↑` => `→`
      case `→` => `↓`
      case `↓` => `←`
      case `←` => `↑`
    }
  }

  val workingDir = Paths.get(".").resolve(Path.of(s"src/${this.getClass.getPackageName}"))
  val inputs     = Seq(
    "example",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def drawPos(p: (Int, Int), heading: `1`.Heading) = {
    g.setCharacter(p.x, p.y, heading.toString.head)
    terminal.setCursorPosition(p.x, p.y)
    terminal.flush()
  }

  val moveTable = Map(
    '<' -> (-1, 0),
    '>' -> (1, 0),
    '^' -> (0, -1),
    'v' -> (0, 1),
  )

  for (src <- srcs) {
    terminal.clearScreen()
    src.getLines()
      .map(_.split(",")).map{ case Array(x, y) => new TerminalPosition(x.toInt, y.toInt) }
      .foreach { tp => g.setCharacter(tp, '#'); Thread.sleep(1); terminal.flush() }
  }
}