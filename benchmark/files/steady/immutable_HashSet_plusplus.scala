/** SI-4642
 *  scala/trunk/src/library/scala/collection/SetLike.scala
 *  Bug:
 *  {{{
 *  def ++ (elems: GenTraversableOnce[A]): This = newBuilder ++= seq ++= elems.seq result
 *  }}}
 *
 *  Fix:
 *  {{{
 *  def ++ (elems: GenTraversableOnce[A]): This = (repr /: elems.seq)(_ + _)
 *  }}}
 *
 */
class immutable_HashSet_plusplus extends scala.tools.sbs.performance.PerformanceBenchmarkTemplate {

  override val measurement = 17

  val set = for (_ <- 1 to 100000) yield collection.immutable.HashSet() // empty set to narrow the difference
  var ls = collection.immutable.IndexedSeq[Int]()

  def init() = ls = collection.immutable.IndexedSeq[Int]()
  def run() {
    var i = 0
    while (i < 230) {
      ls :+= (set map (_ ++ Nil)).length
      i += 1
    }
  }
  def reset() = ()

}
