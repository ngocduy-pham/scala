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

  def init() = set = collection.immutable.HashSet[Int]((1 to 1000000): _*)

  def run() {
    var ls: List[collection.immutable.HashSet[Int]] = Nil
    var i = 0
    while (i < 5500000) {
      ls ::= (set ++ Nil)
      i += 1
    }
    ls foreach (_ => ())
  }

  def reset() = ()

}
