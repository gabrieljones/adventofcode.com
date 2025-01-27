package `16`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.{BoldMode, filterMonospaced}
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalSize, TextColor}

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import scala.util.chaining.*
import lib.*

import java.awt.{Font, GraphicsEnvironment}
import scala.collection.mutable


object `1naive` extends App {

  val fonts: Array[Font] = Array(new Font("MesloLGS NF", Font.PLAIN, 10))
  val defaultTerminalFactory = new DefaultTerminalFactory()
  val fontConfig = new SquareFontConfig(false, BoldMode.EVERYTHING_BUT_SYMBOLS, fonts, Option(8))
  val fontConfiguration: SwingTerminalFontConfiguration = fontConfig
  defaultTerminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration)
  defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(141, 141))

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
//    "example",
//    "example2",
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

  var s = (0, 0)
  var e = (0, 0)
  for (src <- srcs) {
    val map = src.getLines().toArray.map(_.toArray)

    terminal.clearScreen()
    for {
      yi <- map.indices
      xi <- map(0).indices
    } {
      g.setCharacter(xi, yi, map(yi)(xi))
    }
    terminal.flush()

    for {
      yi <- map.indices
      xi <- map(0).indices
    } {
      if (map(yi)(xi) == 'S') {
        s = (xi, yi)
      }
      if (map(yi)(xi) == 'E') {
        e = (xi, yi)
      }
    }
    var pos     = s
    var heading = '>'

    var minScore = Long.MaxValue

    val paths = mutable.Set[String]()
    val maximalPath = Iterator.continually('z').take(200 * 200).mkString
    paths.add(maximalPath)

    def recurse(p: (Int, Int), heading: Heading, map: Array[Array[Char]], checkedPos: Set[(Int, Int)], path: String, score: Long): Unit = {
//      drawPos(p, heading)
      val n = p + heading.d
      map(n) match {
        case _ if score >= minScore =>
        case _ if checkedPos.contains(p) =>
        case _ if path.length > paths.minBy(_.length).length =>
        case '#' =>
          recurse(p, heading.left, map, checkedPos + p, path + "↺" + heading.left.toString, score + 1000)
          recurse(p, heading.right, map, checkedPos + p, path + "↻" + heading.right.toString, score + 1000)
        case 'E' =>
          minScore = Math.min(minScore, score)
          paths += path
          println(s"$path $score")
        case _ =>
          recurse(n, heading, map, checkedPos + p, path + heading.toString, score + 1)
          recurse(n, heading.left, map, checkedPos + p, path + "↺" + heading.left.toString, score + 1000)
          recurse(n, heading.right, map, checkedPos + p, path + "↻" + heading.right.toString, score + 1000)
      }
    }

    recurse(s, Heading.`→`, map, Set(), "→", 0)
    recurse(s, Heading.`↓`, map, Set(), "↻↓", 1000)
    recurse(s, Heading.`←`, map, Set(), "↺↺←", 2000)
    recurse(s, Heading.`↑`, map, Set(), "↺↑", 1000)
    paths.minBy(_.length).map {
      case '↺' | '↻' => 1000
      case _ => 1
    }
      .sum
      .tap(println)

    println(minScore)

  }

}