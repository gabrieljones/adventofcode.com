package lab

object HashcodeFun {
  def main(args: Array[String]): Unit = {
    val hs = for {
      l <-
        ((Int.MinValue * 3L - 8L) to (Int.MinValue * 3L + 2L)) ++
          ((Int.MinValue * 2L - 4L) to (Int.MinValue * 2L + 2L)) ++
          ((Int.MinValue * 1L - 2L) to (Int.MinValue * 1L + 2L)) ++
          ((Int.MinValue * 0L - 2L) to (Int.MinValue * 0L + 2L)) ++
          ((Int.MaxValue * 1L - 2L) to (Int.MaxValue * 1L + 2L)) ++
          ((Int.MaxValue * 2L - 2L) to (Int.MaxValue * 2L + 4L)) ++
          ((Int.MaxValue * 3L - 2L) to (Int.MaxValue * 3L + 8L))
    } yield {
      l -> l.hashCode()
    }

    hs.foreach(println)
  }
}
