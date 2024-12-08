package `08`

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.BoldMode
import com.googlecode.lanterna.terminal.swing.{SquareFontConfig, SwingTerminalFontConfiguration}
import com.googlecode.lanterna.{TerminalSize, TextColor}

import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.util.chaining.*
import lib.*

import java.awt.Font

object `2` extends App {
  val fonts: Array[Font] = Array(new Font("MesloLGS NF", Font.PLAIN, 10))
  val defaultTerminalFactory = new DefaultTerminalFactory()
  val fontConfig = new SquareFontConfig(false, BoldMode.EVERYTHING_BUT_SYMBOLS, fonts, Option(8))
  val fontConfiguration: SwingTerminalFontConfiguration = fontConfig
  defaultTerminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration)
  defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(50, 50))

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

  for (src <- srcs) {
    val map = src.getLines().toArray.map(_.toArray)

    val height = map.length
    val width  = map(0).length

    val antennasGroups = mutable.Map[Char, Set[(Int, Int)]]()

    for {
      ri <- 0 until height
      ci <- 0 until width
    } {
      val cr = (ci, ri)
      map(cr) match {
        case '.' => ()
        case g => antennasGroups(g) = antennasGroups.getOrElse(g, Set()) + cr
      }
    }

    def inBounds(a: (Int, Int)): Boolean = a.c >= 0 && a.c < width && a.r >= 0 && a.r < height

    val antinodes = for {
      (group, antennas) <- antennasGroups
      Seq(a, b) <- antennas.toSeq.combinations(2)
    } yield {
      val (aC, aR) = a
      val (bC, bR) = b
      val dC = bC - aC
      val dR = bR - aR
      val as = ( 0 to 100 ).map(i =>(aC - dC*i, aR - dR*i))
      val bs = ( 0 to 100 ).map(i =>(bC + dC*i, bR + dR*i))
      val g = group
      as ++ bs
    }

    antinodes.flatten.toSet.filter(inBounds).foreach {
      case (c, r) => g.setCharacter(c, r, 'x')
    }


    for {
      ri <- 0 until height
      ci <- 0 until width
      cr = (ci, ri)
      ch = map(cr)
      if ch != '.'
    } {
      g.setCharacter(cr.c, cr.r, map(cr))
    }

    println(antinodes.flatten.toSet.count(inBounds))

    terminal.flush()

  }

}