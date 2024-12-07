package com.googlecode.lanterna.terminal.swing

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.BoldMode

import java.awt.Font

class SquareFontConfig(useAntiAliasing: Boolean, boldMode: AWTTerminalFontConfiguration.BoldMode, fontsInOrderOfPriority: Array[Font], scaleOverride: Option[Int] = None)
  extends SwingTerminalFontConfiguration(useAntiAliasing, boldMode, fontsInOrderOfPriority *) {
  val squareScale: Int = scaleOverride.getOrElse((super.getFontWidth + super.getFontHeight) / 2)
  override def getFontHeight: Int = squareScale
  override def getFontWidth: Int = squareScale
  override def toString: String = {
    s"SquareFontConfig(${super.getFontWidth}, ${super.getFontHeight}, $squareScale, ${super.getFontForCharacter(TextCharacter.DEFAULT_CHARACTER)})"
  }
}
