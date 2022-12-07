package AdventOfCode

import java.io.File

import scala.io.Source

object IntCodeComputer07 {
  type State = (Array[Int], Int)
  sealed trait Mode {
    override def toString: String = this.getClass.getName.dropRight(1).takeRight(1)
  }

  object Mode {
    object P extends Mode
    object I extends Mode
    def apply(i: Int): Mode = i match {
      case 0 => P
      case 1 => I
    }
    def apply(i: Char): Mode = apply(i.asDigit)
    def apply(i: String): Mode = apply(i.toInt)
  }

  object Op {
    def unapply(op: Int): Option[(Mode, Mode, Mode, Int)] = {
      val raw = "%05d".format(op)
      Option((
        Mode(raw(2)),
        Mode(raw(1)),
        Mode(raw(0)),
        raw.takeRight(2).toInt
      ))
    }
  }
  implicit class RAM(val self: Array[Int]) extends AnyVal {
    def apply(mode: Mode)(p: Int): Int = mode match {
      case Mode.P => self(self(p))
      case Mode.I => self(p)
    }
  }

  implicit class Program(val state: State) extends AnyVal {
    //runs program in ram starting at the given instruction pointer
    def run(input: Int): (State,Array[Int], Boolean) = {
      val ram = state._1.toArray //copy supplied ram to prevent unintended mutations of supplied state
      var ip = state._2
      var inputUsed = false
      var outputs = List.empty[Int]
      import Mode._
      while (ram(ip) != 99 && !(ram(ip) == 3 && inputUsed)) { //halt op or needs input and input was already used
        ram(ip) match {
          case Op(m0, m1, P, 1) =>
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(ram(ip + 3)) = a0 + a1
            ip += 4
          case Op(m0, m1, P, 2) =>
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(ram(ip + 3)) = a0 * a1
            ip += 4
          case Op(P, P, P, 3) =>
            ram(ram(ip + 1)) = input
            inputUsed = true
            ip += 2
          case Op(m0, P, P, 4) =>
            outputs = ram(m0)(ip + 1) :: outputs
            ip += 2
          case Op(m0, m1, P, 5) =>
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            if (a0 != 0) ip = a1 else ip += 3
          case Op(m0, m1, P, 6) =>
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            if (a0 == 0) ip = a1 else ip += 3
          case Op(m0, m1, P, 7) =>
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(ram(ip + 3)) = if (a0 < a1) 1 else 0
            ip += 4
          case Op(m0, m1, P, 8) =>
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(ram(ip + 3)) = if (a0 == a1) 1 else 0
            ip += 4
        }
      }
      (
        (ram, ip), //current state of the program
        outputs.toArray.reverse, //all its output this run
        ram(ip) == 99 // true means this program has halted, false means this program is waiting for more input
      )
    }
  }
}

object Day07 extends App {
  println(new File(".").getAbsolutePath)

  val programs = Array(
    "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0",
    "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0",
    "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0",
    Source.fromFile("src/main/scala/AdventOfCode/Day07.input").mkString
  ).map(_.split(",").map(_.toInt))


  def phaseSettingsProduct = for {
    i <- (0 to Integer.parseInt("44444", 5)).iterator
    str = Integer.toString(i, 5)
    leadingZeroes = "%5s".format(str).replaceAll(" ", "0")
    phaseSetting = leadingZeroes.map(_.asDigit)
  } yield {
    phaseSetting
  }

  def phaseSettingsPermutations = (0 until 5).permutations

  def phaseSettingsTest = Iterator.single(IndexedSeq(4,3,2,1,0))

  import IntCodeComputer07.Program
  for {
    (p, i) <- programs.zipWithIndex
  } {
    val runs: Iterator[(IndexedSeq[Int], Array[Int])] = for {
      phaseSetting <- phaseSettingsPermutations
    } yield {
      val outputs = Array.fill(6)(0)
      for {
        amp <- 0 until 5
      } {
        val set = phaseSetting(amp)
        val (initialized, _, false) = (p, 0).run(set)
        val in = outputs(amp)
        val (_, Array(out), true) = initialized.run(in)
        outputs(amp + 1) = out
      }
      (phaseSetting, outputs)
    }
    val (bestSet, bestOut) = runs.maxBy(_._2(5))
    println(s"$i: $bestSet ${bestOut.mkString(",")}")
  }


}

object Day07p2 extends App {

  val programs = Array(
    "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5",
    "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10",
    Source.fromFile("src/main/scala/AdventOfCode/Day07.input").mkString
  ).map(_.split(",").map(_.toInt))

  def phaseSettingsProduct = for {
    i <- (0 to Integer.parseInt("44444", 5)).iterator
    str = Integer.toString(i, 5)
    leadingZeroes = "%5s".format(str).replaceAll(" ", "0")
    phaseSetting = leadingZeroes.map(_.asDigit)
  } yield {
    phaseSetting
  }

  def phaseSettingsPermutations = (5 until 10).permutations

//  def phaseSettingsTest = Iterator.single(IndexedSeq(9,8,7,6,5))
  def phaseSettingsTest = Array(IndexedSeq(9,8,7,6,5), IndexedSeq(9,7,8,5,6)).iterator

  import IntCodeComputer07.Program
  import IntCodeComputer07.State
  for {
    (p, i) <- programs.zipWithIndex
  } {
    val runs: Iterator[(IndexedSeq[Int], Array[Int])] = for {
      phaseSetting <- phaseSettingsPermutations
    } yield {
      var io = 0 :: Nil
      var states: Seq[State] = for {
        amp <- 0 until 5
      } yield {
        val set = phaseSetting(amp)
        val (initialized, _, false) = (p, 0).run(set)
        initialized
      }
      var haltedCount = 0
      var runCount = 0
      while (haltedCount < 5) {
        val Seq(prevState, remain@_*) = states
        val (nextState, Array(out), halted) = prevState.run(io.head)
        io = out :: io
        if (halted) haltedCount += 1
        runCount += 1
        states = remain :+ nextState
      }
//      println(runCount)
      (phaseSetting, io.toArray)
    }
    val (bestSet, bestOut) = runs.maxBy(_._2.head)
    println(s"$i: $bestSet ${bestOut.mkString(",")}")
  }

}