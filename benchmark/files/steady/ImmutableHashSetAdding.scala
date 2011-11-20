/** scala/trunk/src/library/scala/collection/SetLike.scala
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
class ImmutableHashSetAdding extends scala.tools.sbs.performance.PerformanceBenchmarkTemplate {

  override val measurement = 17
  override val timeout = 60 * 1000

  val set = for (_ <- 1 to 100000) yield (collection.immutable.HashSet((0 to 1): _*))
  var ls = collection.immutable.IndexedSeq[AnyRef]()

  def init() = ls = collection.immutable.IndexedSeq[AnyRef]()
  def run() {
    var i = 0
    while (i < 23) {
      ls ++= set map (_ ++ Nil)
      i += 1
    }
  }
  def reset() = ()

}
