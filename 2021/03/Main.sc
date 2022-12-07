import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.util.chaining._

val workingDir = new File("/Users/gjone12/Projects/gradle-scala-thirteen/src/main/scala/AdventOfCode/2021")
val input = new File(workingDir, "/03/input")
def src = Source.fromFile(input)
def entries = src.getLines().toSeq

def padBin(w: Int)(i: Int) = String.format("%" + w + "s", i.toBinaryString).replace(' ', '0')

{
  val bs = entries.transpose

  val q = bs.head.size
  val bq = bs.size
  val bcs = bs.map(_.map(_.asDigit).sum)

  val g = bcs.reverse.zipWithIndex.foldLeft(0){
    case (z, (c, i)) if c > q / 2 => z | 1 << i
    case (z, _) => z
  }

  val mask = (1 << bq) - 1
  val e = g ^ mask
  padBin(12)(mask)
  padBin(12)(g)
  padBin(12)(e)
  g * e
}

{
  def filter(fn: (Int, Int) => Boolean, i: Int)(is: Seq[String]): Seq[String] = {
    val (s1, s0) = is.partition(_(i) == '1')
//    println(s1.transpose.map(_.map(_.asDigit).sum))
//    println(s0.transpose.map(_.map(_.asDigit).sum))
    val r = if (fn(s1.size, s0.size)) s1 else s0
//    println((f"$i%3s", f"${s1.size}%3s", f"${s0.size}%3s", r))
    r
  }
  val w = entries.head.length
  val oxy = (0 until w).foldLeft(entries){
    case (z, _) if z.size == 1 => z
    case (z, i) => filter(_ >= _, i)(z)
  }.head
    .pipe(Integer.parseInt(_, 2))
  val co2 = (0 until w).foldLeft(entries){
    case (z, _) if z.size == 1 => z
    case (z, i) => filter(_ < _, i)(z)
  }.head
    .pipe(Integer.parseInt(_, 2))


  oxy * co2
}