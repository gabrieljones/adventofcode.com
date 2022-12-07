package AdventOfCode

import java.awt.{Color, Dimension, Graphics}

import AdventOfCode.Day11.img
import javax.swing.JPanel

import scala.io.Source
import scala.math.BigInt

object IntCodeComputer13 {
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

object Day13 extends App {

  private val source = Source.fromFile("src/main/scala/AdventOfCode/Day13.input")
  val program: Array[BigInt] = source.mkString.split(",").map(BigInt.apply) ++ Array.fill(2000)(BigInt(0))
  source.close()
  import IntCodeComputer13.Program
  program(0) = 2 //Memory address 0 represents the number of quarters that have been inserted; set it to 2 to play for free.
  val initialState = (program, 0, 0)
  var p = initialState

  var halted = false: Boolean

  var paddleDir = 0
  val canvas = Array.fill(22, 43)(0)
  var score = 0
  val scale = 16
  val img = new JPanel {
    // paint the canvas
    override def paintComponent(g: Graphics): Unit = {
      for {
        (r, y) <- canvas.zipWithIndex
        (c, x) <- r.zipWithIndex
      } {
        val color = c match {
          case 0 => // is an empty tile. No game object appears in this tile.
            Color.white
          case 1 => // is a wall tile. Walls are indestructible barriers.
            Color.black
          case 2 => // is a block tile. Blocks can be broken by the ball.
            Color.green
          case 3 => // is a horizontal paddle tile. The paddle is indestructible.
            Color.red
          case 4 => // is a ball tile. The ball moves diagonally and bounces off objects.
            Color.blue
        }
        g.setColor(color)
        g.fillRect(x.toInt * scale - 1, y.toInt * scale - 1, scale, scale)
      }
      g.drawString(score.toString, 32, 30)
    }

  }
  {
    import javax.swing._, java.awt.event._
    val frame = new JFrame()
    val sizeD = new Dimension(scale * 43, scale * 22)
    img.setSize(sizeD)
    img.setPreferredSize(sizeD)
    frame.getContentPane.add(img)
    frame.pack()
    frame.setVisible(true)
    frame.addKeyListener( new KeyListener {
      override def keyTyped(e: KeyEvent): Unit = {}

      override def keyPressed(e: KeyEvent): Unit = { e.getKeyChar match {
        case 'a' => paddleDir = -1
        case 'd' => paddleDir = 1
      }}

      override def keyReleased(e: KeyEvent): Unit = { paddleDir = 0 }
    })
  }

  var pX = 0
  var bX = 0
//  println(s"${p._2} ${p._3}")
  while(!halted) {
    val (_p, out, _halted) = p.run((bX - pX) % 2)
    for {
      Array(x,y,t) <- out.grouped(3)
    } {
      if (x > -1) {
        canvas(y.toInt)(x.toInt) = t.toInt
        if (t.toInt == 3) pX = x.toInt
        if (t.toInt == 4) bX = x.toInt
      } else {
        score = t.toInt
      }
    }
    println(s"$bX, $pX : ${(bX - pX) % 2}")
    p = _p; halted = _halted
    img.repaint()
    Thread.sleep(1000 / 60)
  }
}
