/** library/scala/collection/SeqLike.scala
 *
 *  Bug: SeqLike_bug.scala
 */
object List_indexOfSlice {

  val ls = (1 to 1000000).toList
  val seq = (30 to 73)
  var i = collection.mutable.ArrayBuffer[Int]()

  def main(args: Array[String]) {
    for (_ <- 1 to 10) i :+= (ls indexOfSlice seq)
    i.clear
  }

}
