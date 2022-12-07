
def phaseSettings = for {
  i <- (0 to Integer.parseInt("4444", 5)).iterator
  str = Integer.toString(i, 5)
  leadingZeroes = "%4s".format(str).replaceAll(" ", "0")
  phaseSetting = leadingZeroes.map(_.asDigit)
} yield {
  phaseSetting
}

phaseSettings.take(10).toList

Math.atan2(0, -100)

(-1 + 4) % 4