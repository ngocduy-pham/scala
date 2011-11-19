/** scala/trunk/src/library/scala/collection/mutable/ListBuffer.scala
 *  Bug:
 *  {{{
 *  // Nothing
 *  }}}
 *
 *  Fix:
 *  {{{
 *  override def size = length
 *  }}}
 *
 */
object ListBuffer_size extends scala.tools.sbs.performance.PerformanceBenchmarkTemplate {

  override val measurement = 11

  val lb = (for(_ <- 1 to 100000) yield collection.mutable.ListBuffer((0 to 15): _*)).toList

  def init() = ()
  def run() {
    val ls = (1 to 15000) map (lb map (_ size))
    ls foreach (_ => ())
  }
  def reset() = ()

}
