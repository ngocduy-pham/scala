/** library/scala/collection/TraversableOnce.scala
 *  Bug
 *  {{{
 *  def flatten: Iterator[A] = travs.foldLeft(Iterator.empty: Iterator[A])(_ ++ _)
 *  }}}
 *
 *  Fix
 *  {{{
 *  def flatten: Iterator[A] = new AbstractIterator[A] {
 *    val its = travs.toIterator
 *    private var it: Iterator[A] = Iterator.empty
 *    def hasNext: Boolean = it.hasNext || its.hasNext && { it = its.next.toIterator; hasNext }
 *    def next(): A = if (hasNext) it.next() else Iterator.empty.next()
 *  }
 *  }}}
 */
object Iterator_flatten {

  def gen(depth: Int, span: Int): Iterator[Int] = {
    if (depth == 0) {
      (1 to span).iterator
    }
    else {
      ((1 to span) map { _ => gen(depth - 1, span) }).iterator.flatten
    }
  }

  def main(args: Array[String]) {
    val ls = for(_ <- 1 to 30) yield gen(1, 679).length
    ls foreach (_ => ())
  }

}
