import scala.util.Random
import scala.util.chaining.scalaUtilChainingOps

object RandomSum extends App {
  val ri: Iterator[Array[Double]] = Iterator.continually {
    //fast random spherical 3d unit vector
    val x = Random.nextDouble() * 2 - 1
    val y = Random.nextDouble() * 2 - 1
    val z = Random.nextDouble() * 2 - 1
    val d = math.sqrt(x * x + y * y + z * z)
    Array(x / d, y / d, z / d)
  }

  val sums: Array[Double] = Array.fill(3)(0.0)

  ri
  .take(1024* 1024 * 1024)
    .foreach(a => a.zipWithIndex.foreach((e, i) => sums(i) += e))

  sums.mkString(",").tap(println)

}
