package AdventOfCode

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object Day10 extends App {

  private val source = Source.fromFile("src/main/scala/AdventOfCode/Day10.input")
  val asteroids: Array[(Int, Int)] = for {
    (row, r) <- source.mkString.split("\n").zipWithIndex
    (cell, c) <- row.zipWithIndex
    if cell == '#'
  } yield {
    (c,r)
  }
  source.close()
  val thetas: Array[((Int, Int), Double)] = for {
    (sx, sy) <- asteroids
    (ax, ay) <- asteroids
  } yield {
    ((sx, sy),Math.atan2(sy - ay, sx - ax))
  }
  val best = thetas.toSet.groupBy((p: ((Int, Int), Double)) => p._1).maxBy(_._2.size)
  println(best)
  println(best._2.size)

  val station = (37,25)
  def sq(x: Int): Long = x * x
  implicit class Trig(val self: (Int, Int)) extends AnyVal {
    def theta(that: (Int, Int)): Double = {
      val (sx, sy) = self
      val (tx, ty) = that
      Math.atan2(tx - sx, ty - sy)
    }
    def dist(that: (Int, Int)): Double = {
      val (sx, sy) = self
      val (tx, ty) = that
      Math.sqrt(sq(ty - sy) + sq(sx - tx))
    }
  }
  import Ordering.Double.TotalOrdering
  val grouped: Map[Double, Array[((Int, Int), (Double, Double))]] = asteroids.map(a => a -> (-station.theta(a), station.dist(a))).sortBy(_._2).groupBy(_._2._1)
  val arr2: Array[Array[((Int, Int), (Double, Double))]] = grouped.toArray.sortBy(_._1).map(_._2)
  def rectify[T](array: Array[Array[T]]): Array[Array[Option[T]]] = {
    val max = array.map(_.length).max
    val ns = Array.fill(max)(None)
    array.map(_.map(Option.apply) ++ ns).map(_.take(max))
  }
  val arrR = rectify(arr2)
  val arrF: Array[((Int, Int), (Double, Double))] = arrR.transpose.flatten.flatten
  println(arrF.take(10).mkString(","))
  println(arrF(300))
  val a200 = arrF(199)
  println(a200)
  println(a200._1._1 * 100 + a200._1._2)
  val numMap = Array.fill(48)(Array.fill(48)("   "))
  for {
    (((x, y), (_, _)), i) <- arrF.zipWithIndex
  } {
    numMap(y)(x) = "%03d".format(i)
  }
  numMap(25)(37) = "SSS"
  println(numMap.map(_.mkString).mkString("\n"))
}
