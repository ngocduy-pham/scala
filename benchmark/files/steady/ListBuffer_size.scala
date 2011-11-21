/** SI-4933
 *  library/scala/collection/mutable/ListBuffer.scala
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

  override val measurement = 21

  val lb = (for(_ <- 1 to 100000) yield collection.mutable.ListBuffer((0 to 40): _*)).toList

  def init() = ()
  def run() {
    val ls = (1 to 20000) map (lb map (_ size))
    ls foreach (_ => ())
  }
  def reset() = ()

}
