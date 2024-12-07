package `06`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.{BoldMode, filterMonospaced}
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalSize, TextColor}

import java.awt.{Font, GraphicsEnvironment}
import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.util.chaining.*

object `1` extends App {

  val fonts: Array[Font] = GraphicsEnvironment
    .getLocalGraphicsEnvironment
    .getAvailableFontFamilyNames
    .map(new Font(_, Font.PLAIN, 10))
    .pipe(fs => filterMonospaced(fs *))
    .tap(_.mkString("\n").tap(println))
    .drop(4)
    .pipe(_ => Array(new Font("MesloLGS NF", Font.PLAIN, 10))) //effectively ignores all the code above this line
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
//    "example-alex",
//    "example-alex2",
//    "example-alex3",
    "input",
  )
    .map(Path.of(_)).map(workingDir.resolve).map(_.toFile)

  def srcs = inputs.map(Source.fromFile)

  def dirsInit: Iterator[(Int, Int)] = Iterator.continually(
    Seq(
      ( 0,-1), //up
      ( 1, 0), //right
      ( 0, 1), //down
      (-1, 0),
    )
  ).flatten

  extension (co: (Int, Int)) {
    def +(other: (Int, Int)): (Int, Int) = (co._1 + other._1, co._2 + other._2)
    def c: Int = co._1
    def r: Int = co._2
  }

  extension (map: Array[Array[Char]]) {
    def apply(co: (Int, Int)): Char = map(co.r)(co.c)
    def update(co: (Int, Int), ch: Char): Unit = map(co.r)(co.c) = ch
  }

  for (src <- srcs) {
    val dirs = dirsInit
    val map    = src.getLines().toArray.map(_.toArray)
    val height = map.length
    val width  = map(0).length
    var cr = (0, 0)
//    var r      = 0
//    var c      = 0
    for {
      ri <- 0 until height
      ci <- 0 until width
    } {
      g.setCharacter(ci, ri, map(ri)(ci))
//      val newChar = map((ci, ri)) match {
//        case '#' => '█'
//        case '.' => '.'
//        case '^' => '●'
//        case _ => ' '
//      }
//      g.setCharacter(ci, ri, newChar)
      terminal.setCursorPosition(ci, ri)
      if (map(ci,ri) == '^') {
        cr = (ci,ri)
      }
    }
    terminal.flush()
    map(cr) = '.'

//    g.putString(0, 0, fontConfig.toString)

    var dir       = dirs.next()
    val positions = mutable.Set[(Int, Int)]()
    try {
      while (true) {
//        println(s"$cr $dir")
        map(cr) = 'x'
        positions.add(cr)
//        Thread.sleep(1)
        val ch = g.getCharacter(cr.c, cr.r)
        g.setCharacter(cr.c, cr.r, ch.withForegroundColor(TextColor.ANSI.RED_BRIGHT))
        terminal.setCursorPosition(cr.c, cr.r)
        terminal.flush()
        map(cr + dir) match {
          case '#' => dir = dirs.next()
          case _ => cr += dir
        }
      }
    } catch {
      case _: Exception => ()
    }
//    Thread.sleep(10000)
//    terminal.close()
    println(positions.size)
    map.flatten.count(_ == 'x').tap(println)
    println(fontConfig)
  }
}