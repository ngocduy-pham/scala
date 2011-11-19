/** scala/trunk/src/library/scala/collection/mutable/ArrayBuilder.scala
 *
 *  Bug:
 *  {{{
 *  private def ensureSize(size: Int) {
 *    if (capacity == 0) resize(16)
 *    if (capacity < size) {
 *      var newsize = capacity * 2
 *      while (newsize < size) newsize *= 2
 *      resize(newsize)
 *    }
 *  }
 *
 *  }}}
 *
 *  Fix:
 *  {{{
 *  private def ensureSize(size: Int) {
 *    if (capacity < size || capacity == 0) {
 *      var newsize = if (capacity == 0) 16 else capacity * 2
 *      while (newsize < size) newsize *= 2
 *      resize(newsize)
 *    }
 *  }
 *
 *  }}}
 */
object ArrayBuilderEnsureSize {

  def main(args: Array[String]) {
    var ls = List[collection.mutable.ArrayBuilder[Int]]()
    var i = 0
    while (i < 200000) {
      val builder = collection.mutable.ArrayBuilder.make[Int]
      ls ::= (builder += i)
      i += 1
    }
  }

}
