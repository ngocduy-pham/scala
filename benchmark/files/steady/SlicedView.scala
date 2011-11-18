/** scala/trunk/src/library/scala/collection/SeqViewLike.scala
 *  Bug:
 *  {{{
 *  trait Appended[B >: A] extends Transformed[B] with super.Appended[B] {
 *    override def iterator = self.iterator ++ rest.toIterable.iterator
 *  }
 *  }}}
 *
 *  Fix:
 *  {{{
 *  trait Appended[B >: A] extends super.Appended[B] with Transformed[B] {
 *    def iterator = self.iterator ++ rest
 *  }
 *  }}}
 *
 */
object SlicedView {

  def main(args: Array[String]) {
    val million = Array.range(1, 1000000)
    val veryslow = million.view.slice(999990, 999999)
    var sum = 0
    veryslow.foreach(sum += _)
  }

}
