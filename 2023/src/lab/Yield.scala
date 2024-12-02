package lab

object Yield {

  @inline
  def repeat(fn: => Unit): Unit = {
    var i = Int.MinValue
//    var j = Int.MinValue
//    while (j < Int.MaxValue) {
      while (i < Int.MaxValue) {
        fn
        //Thread.`yield`()
        i += 1
      }
//      j += 1
//    }
  }

  val fnLongHashcode: Runnable = () => {
    var x = java.util.Random().nextLong()
    repeat {
      x = java.lang.Long.hashCode(x)
    }
    println(x)
  }

  val fnDoubleInverse: Runnable = () => {
    var x = java.util.Random().nextDouble()
    repeat {
      x = Math.sqrt(1 / x) + ((java.lang.Double.doubleToRawLongBits(x) & 0x000fffffffffffffL) << 10)
    }
    println(x)
  }

  def main(args: Array[String]): Unit = {
    val ts = for (j <- 1 to 1 by -1) yield {
      System.gc()
      System.runFinalization()
      val threads = for (i <- 1 to j) yield new Thread(fnDoubleInverse)
      val start = System.nanoTime()
      threads.foreach(_.start())
      threads.foreach(_.join())
      val end = System.nanoTime()
      j -> (end - start)
    }

    ts.map { case (j, e) => s"Elapsed($j): ${e / 1_000_000_000.0}s" }.foreach(println)
  }
}
