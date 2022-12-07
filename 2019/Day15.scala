package AdventOfCode.`2019`

import java.awt.{Color, Dimension, Graphics}

import javax.swing.JPanel

import scala.io.Source
import scala.math.BigInt
import scala.util.Random

object IntCodeComputer15 {
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

object Day15 extends App {

  private val source = Source.fromFile("src/main/scala/AdventOfCode/2019/Day15.input")
  val program: Array[BigInt] = source.mkString.split(",").map(BigInt.apply) ++ Array.fill(2000)(BigInt(0))
  source.close()
  import IntCodeComputer15.Program
  val initialState = (program, 0, 0)
  var p = initialState

  var halted = false: Boolean

  type Loc = (Int, Int)
  type Heading = Int

  val headingVectors = Array(
    ( 0, 0),
    ( 0,-1), //N
    ( 0, 1), //S
    (-1, 0), //W
    ( 1, 0), //E
  )

  implicit class LocOps(val self: Loc) extends AnyVal {
    def +(h: Heading): Loc = (self._1 + headingVectors(h)._1, self._2 + headingVectors(h)._2)
  }

  val canvas = Array.fill(64, 64)(-1)
  var l: Loc = (32,32)
  val scale = 16
  val img = new JPanel {
    // paint the canvas
    override def paintComponent(g: Graphics): Unit = {
      for {
        (r, y) <- canvas.zipWithIndex
        (c, x) <- r.zipWithIndex
      } {
        val color = c match {
          case -1 => // is an empty tile. No game object appears in this tile.
            Color.white
          case 0 => // is an empty tile. No game object appears in this tile.
            Color.black
          case 1 => // is a wall tile. Walls are indestructible barriers.
            Color.gray
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
      g.setColor(Color.blue)
      g.fillRect(l._1 * scale - 1, l._2 * scale - 1, scale, scale)
      g.setColor(Color.red)
      g.fillRect(32 * scale - 1, 32 * scale - 1, scale, scale)
    }
  }

  {
    import javax.swing._, java.awt.event._
    val frame = new JFrame()
    val sizeD = new Dimension(scale * 64, scale * 64)
    img.setSize(sizeD)
    img.setPreferredSize(sizeD)
    frame.getContentPane.add(img)
    frame.pack()
    frame.setVisible(true)
    frame.addKeyListener( new KeyListener {
      override def keyTyped(e: KeyEvent): Unit = {}

      override def keyPressed(e: KeyEvent): Unit = {}

      override def keyReleased(e: KeyEvent): Unit = {
        val m = e.getKeyChar match {
          case 'w' => 1
          case 's' => 2
          case 'a' => 3
          case 'd' => 4
          case  _  => 0
        }
        move(m)
      }
    })
  }

  def move(m: Int) = {
    if (m > 0) {
      val (_p, Array(status), _halted) = p.run(m)
      p = _p; halted = _halted
      (m, status.toInt) match {
        case (m, 0) => val w = l + m; canvas(w._2)(w._1) = 0
        case (m, 1) => l = l + m; canvas(l._2)(l._1) = 1
        case (m, 2) => l = l + m; canvas(l._2)(l._1) = 2
        case (m, s) => ???
      }
      img.repaint()
      status.toInt
    } else 0
  }
  var found = false
  var dir = 1
  while(!found) {
    val status = move(dir)
    dir = Random.nextInt(4) + 1
//    if (status == 0 & canvas() != 0) dir = (dir + 2) % 4 + 1
    found = (status == 2)
  }

}
