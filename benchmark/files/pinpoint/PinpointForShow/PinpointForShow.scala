import scala.tools.nsc.io.Directory

class PinpointForShow extends scala.tools.sbs.pinpoint.PinpointBenchmarkTemplate {

  override val timeout = -1

  override val measurement = 17

  override val pinpointClass = "PinpointForShow"

  override val pinpointMethod = "run"

  override val pinpointExclude = List("java.*")

  override val pinpointDepth = 2

  override val pinpointPrevious = Directory("benchmark/files/bin/pinpointprevious")

  // force object construction
  val failure = ListBuffer_size
  val ok = Iterator_flatten

  def init() = ()
  def run() = {
    bridge
    ok.main
  }
  def reset() = ()

  def bridge = {
    foo
    failure.run
  }
  def foo = Thread sleep 50
  def bar = Thread sleep 40

}

object ListBuffer_size {

  val lb = (for (_ <- 1 to 100000) yield collection.mutable.ListBuffer((0 to 50): _*)).toList

  def init() = ()
  def run() {
    val ls = (1 to 15000) map (lb map (_ size))
    ls foreach (_ => ())
  }

}

object Iterator_flatten {

  def gen(depth: Int, span: Int): Iterator[Int] =
    if (depth == 0) (1 to span).iterator
    else ((1 to span) map (_ => gen(depth - 1, span))).iterator.flatten

  def main {
    val ls = for (_ <- 1 to 300000) yield gen(1, 3).length
    ls foreach (_ => ())
  }

}
