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

  override val measurement = 11

  override val timeout = 180000

  var set = collection.immutable.HashSet[Int]()

  def init() = set ++= (1 to 1000000).toList

  def run() {
    var i: List[collection.immutable.HashSet[Int]] = Nil
    for (_ <- 1 to 2000000) i ::= (set ++ Nil)
  }

  def reset() = set = collection.immutable.HashSet[Int]()

}
