package AdventOfCode

import java.io.File

import scala.io.Source
import scala.math.BigInt

object IntCodeComputer09 {
  type InstructionPointer = Int
  type RelativeBase = Int
  type State = (Array[BigInt], InstructionPointer, RelativeBase)
  sealed trait Mode {
    override def toString: String = this.getClass.getName.dropRight(1).takeRight(1)
  }

  object Mode {
    object P extends Mode
    object I extends Mode
    object R extends Mode
    def apply(i: Int): Mode = i match {
      case 0 => P
      case 1 => I
      case 2 => R
    }
    def apply(i: Char): Mode = apply(i.asDigit)
    def apply(i: String): Mode = apply(i.toInt)
  }

  object Op {
    def unapply(op: BigInt): Option[(Mode, Mode, Mode, Int)] = {
      val raw = "%05d".format(op)
      Option((
        Mode(raw(2)),
        Mode(raw(1)),
        Mode(raw(0)),
        raw.takeRight(2).toInt
      ))
    }
  }

  implicit class ExactToInt(val self: BigInt) extends AnyVal {
    def toIntE = self.bigInteger.intValueExact()
  }
  implicit class RAM(val self: Array[BigInt]) extends AnyVal {
    def apply(mode: Mode)(p: InstructionPointer)(implicit relBase: RelativeBase): BigInt = mode match {
      case Mode.P => self(self(p).toIntE)
      case Mode.I => self(p)
      case Mode.R => self(self(p).toIntE+relBase)
    }
    def update(mode: Mode, p: InstructionPointer, v: BigInt)(implicit relBase: RelativeBase): Unit = mode match {
      case Mode.P => self(self(p).toIntE) = v
      case Mode.I => throw new UnsupportedOperationException()
      case Mode.R => self(self(p).toIntE+relBase) = v
    }
  }

  implicit class Program(val state: State) extends AnyVal {
    //runs program in ram starting at the given instruction pointer
    def run(input: BigInt): (State,Array[BigInt], Boolean) = {
      val ram: Array[BigInt] = state._1.toArray //copy supplied ram to prevent unintended mutations of supplied state
      var ip: InstructionPointer = state._2 //instruction pointer
      implicit var rb: RelativeBase = state._3 //relative base
      var inputUsed = false
      var outputs = List.empty[BigInt]
      import Mode._
      while (ram(ip) != 99 && !(ram(ip) == 3 && inputUsed)) { //halt op or needs input and input was already used
        ram(ip) match {
          case Op(m0, m1, m2, 1) => //add
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(m2, ip + 3) = a0 + a1
            ip += 4
          case Op(m0, m1, m2, 2) => //multiply
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(m2, ip + 3) = a0 * a1
            ip += 4
          case Op(m0, P, P, 3) => //input
            ram(m0, ip + 1) = input
            inputUsed = true
            ip += 2
          case Op(m0, P, P, 4) => //output
            outputs = ram(m0)(ip + 1) :: outputs
            ip += 2
          case Op(m0, m1, P, 5) => //jump-if-true
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ip = if (a0 != 0) a1.toIntE else ip + 3
          case Op(m0, m1, P, 6) => //jump-if-false
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ip = if (a0 == 0) a1.toIntE else ip + 3
          case Op(m0, m1, m2, 7) => //less than
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(m2, ip + 3) = if (a0 < a1) BigInt(1)else BigInt(0)
            ip += 4
          case Op(m0, m1, m2, 8) => //equals
            val a0 = ram(m0)(ip + 1)
            val a1 = ram(m1)(ip + 2)
            ram(m2, ip + 3) = if (a0 == a1) BigInt(1)else BigInt(0)
            ip += 4
          case Op(m0, P, P, 9) =>
            rb += ram(m0)(ip + 1).toIntE
            ip += 2
        }
      }
      (
        (ram, ip, rb), //current state of the program
        outputs.toArray.reverse, //all its output this run
        ram(ip) == 99 // true means this program has halted, false means this program is waiting for more input
      )
    }
  }
}

object Day09 extends App {
  println(new File(".").getAbsolutePath)

  val programs = Array(
    "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99",
    "1102,34915192,34915192,7,4,7,99,0",
    "104,1125899906842624,99",
    Source.fromFile("src/main/scala/AdventOfCode/Day09.input").mkString
  ).map(_.split(",").map(BigInt.apply))

  import IntCodeComputer09.Program
  for {
    (program, i) <- programs.zipWithIndex
  } {
    val expandedRam = program ++ Array.fill(2000)(BigInt(0))
    val (_, out, _) = (expandedRam, 0, 0).run(2)
    println(s"$i: ${out.mkString(",")}")
  }
}