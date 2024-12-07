package `06`

import com.googlecode.lanterna.{TerminalSize, TextColor}
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.{BoldMode, filterMonospaced, selectDefaultFont}
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration

import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.util.chaining.*

class MyFontConfig() extends SwingTerminalFontConfiguration(false, BoldMode.EVERYTHING_BUT_SYMBOLS, filterMonospaced(selectDefaultFont(24) *) *) {
  override def getFontHeight: Int = getFontWidth
//  override def getFontWidth: Int = getFontHeight
}
object `1` extends App {

  val defaultTerminalFactory = new DefaultTerminalFactory()
  private val fontConfiguration: SwingTerminalFontConfiguration = new MyFontConfig
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
      terminal.setCursorPosition(ci, ri)
      if (map(ci,ri) == '^') {
        cr = (ci,ri)
      }
    }
    terminal.flush()
    map(cr) = '.'

    var dir       = dirs.next()
    val positions = mutable.Set[(Int, Int)]()
    try {
      while (true) {
        println(s"$cr $dir")
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
  }
}