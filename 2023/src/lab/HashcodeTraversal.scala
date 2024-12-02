package lab

object HashcodeTraversal {


  def main(args: Array[String]): Unit = {
    val hs = for {
      b: Byte <- (Byte.MinValue to Byte.MaxValue).map(_.toByte)
      i: Int = b.toInt
      l: Long = b.toLong
    } yield {
      (b -> b.hashCode(), i -> i.hashCode(), l -> l.hashCode())
    }

    hs.foreach(println)
  }

}
