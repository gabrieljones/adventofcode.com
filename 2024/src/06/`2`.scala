package `06`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.BoldMode
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalSize, TextCharacter, TextColor}
import lib.*

import java.awt.Font
import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.mutable
import scala.io.Source

object `2` extends App {

  val fonts: Array[Font] = Array(new Font("MesloLGS NF", Font.PLAIN, 10))
  val defaultTerminalFactory = new DefaultTerminalFactory()
  val fontConfig = new SquareFontConfig(false, BoldMode.EVERYTHING_BUT_SYMBOLS, fonts, Option(8))
  val fontConfiguration: SwingTerminalFontConfiguration = fontConfig
  defaultTerminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration)
  defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(130, 130))

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

  def srcs = inputs.map(Source.fromFile)

  def dirsInit: Iterator[(Int, Int)] = Iterator.continually(
    Seq(
      ( 0,-1), //up
      ( 1, 0), //right
      ( 0, 1), //down
      (-1, 0), //left
    )
  ).flatten

  val visited = new TextCharacter('x', TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.DEFAULT)

  val colors = TextColor.ANSI.values().filterNot(c => c.getRed == c.getGreen && c.getRed == c.getBlue).toSeq
  val colorsI = Iterator.continually(colors).flatten


  for (src <- srcs) {
    val dirs = dirsInit
    val map    = src.getLines().toArray.map(_.toArray)
    val height = map.length
    val width  = map(0).length
    var cr = (0, 0)
    for {
      ri <- 0 until height
      ci <- 0 until width
    } {
      g.setCharacter(ci, ri, map(ri)(ci))
      terminal.setCursorPosition(ci, ri)
      if (map(ci,ri) == '^') {
        cr = (ci,ri)
      }
    }
    val init = cr
    terminal.flush()
    var dir       = dirs.next()
    val positions = mutable.Set[(Int, Int)]()
    try {
      while (true) {
        positions.add(cr)
        g.setCharacter(cr.c, cr.r, visited)
        map(cr + dir) match {
          case '#' => dir = dirs.next()
          case _ => cr += dir
        }
      }
    } catch {
      case _: Exception => ()
    }
    terminal.flush()
    println(positions.size)
    var loops = 0
    for {
      ob <- positions
    } {
//      print(s"$ob")
      map(ob) = '#'
      val visited2 = new TextCharacter('x', colorsI.next(), TextColor.ANSI.DEFAULT)
      g.setCharacter(ob.c, ob.r, '#')
      terminal.flush()
      val dirs = dirsInit
      var dir       = dirs.next()
      val orientations = mutable.Set[((Int, Int), (Int, Int))]()
      var prevCr = ob
      cr = init
      try {
        while (!orientations((cr, dir)) || prevCr == cr) {
          prevCr = cr
          orientations.add((cr, dir))
          g.setCharacter(cr.c, cr.r, visited2)
          map(cr + dir) match {
            case '#' => dir = dirs.next()
            case _ => cr += dir
          }
        }
        terminal.flush()
        loops += 1
//        print(" looped")
      } catch {
        case _: Exception => ()
      }
//      println()
      map(ob) = '.' // reset
      g.setCharacter(ob.c, ob.r, '.')
    }
    println(s"loops: $loops")
  }
}