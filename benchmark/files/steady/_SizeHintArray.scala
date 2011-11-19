import scala.collection.mutable.ArrayBuffer

/** scala/trunk/src/library/scala/collection/mutable/ArrayBuffer.scala
 *  Bug:
 *  {{{
 *  // line 66
 *  Array.copy(array, 0, newarray, 0, size0)
 *  }}}
 *
 *  Fix:
 *  {{{
 *  // line 66
 *  compat.Platform.arraycopy(array, 0, newarray, 0, size0)
 *  }}}
 *
 */
object SizeHintArray {
  
  def main(args: Array[String]) {
    val ab = ArrayBuffer((1 to 10): _*) map (new Integer(_))
    var ls = List[ArrayBuffer[Integer]]()
    var i = 0
    while (i < 10000) {
      ab sizeHint (i + 11)
      ls ::= ab.result
      i += 1
    }
  }

}
