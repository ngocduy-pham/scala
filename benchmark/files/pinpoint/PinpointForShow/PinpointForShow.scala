import scala.tools.nsc.io.Directory

class PinpointForShow extends scala.tools.sbs.pinpoint.PinpointBenchmarkTemplate {

  override val timeout = -1
  
  override val measurement = 21

  override val pinpointClass = "PinpointForShow"

  override val pinpointMethod = "run"

  override val pinpointExclude = List("java.*")

  override val pinpointDepth = 2

  override val pinpointPrevious = Directory("benchmark/files/bin/pinpointprevious")

  // force object construction
  val failure = ListBuffer_size
  val ok = TraversableOnce_toArray

  def init() = ()
  def run() = {
    bridge
    stupid
    ok main null
  }
  def reset() = ()

  def bridge = failure.run
  def stupid = Thread sleep 10

}
