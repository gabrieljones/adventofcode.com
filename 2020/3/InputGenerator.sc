import scala.collection.BitSet
import scala.util.Random

val seed = 0
val width = 31
val height = 323
val random = new Random(seed)
Iterator.continually(random.nextBoolean())
  .map{ if (_) '#' else '.' }
  .grouped(width)
  .take(height)
  .flatMap(_.appended('\n'))
  .mkString

width * height
width * height / 8 * 8

new Random(seed)
  .nextBytes(width * height / 8 + 1)
  .flatMap(_.toBinaryString)
  .length

(Byte.MinValue to Byte.MaxValue)
  .map(_.toByte)
  .drop(127)
  .map(_.toBinaryString)

BitSet.newBuilder.