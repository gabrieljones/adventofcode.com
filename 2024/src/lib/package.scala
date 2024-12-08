package object lib {
  extension (co: (Int, Int)) {
    def +(other: (Int, Int)): (Int, Int) = (co._1 + other._1, co._2 + other._2)
    def c: Int = co._1
    def r: Int = co._2
    def vert: Boolean = co.c == 0
  }

  extension (map: Array[Array[Char]]) {
    def apply(co: (Int, Int)): Char = map(co.r)(co.c)
    def update(co: (Int, Int), ch: Char): Unit = map(co.r)(co.c) = ch
  }
}
