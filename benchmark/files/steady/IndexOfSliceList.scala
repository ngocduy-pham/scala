object IndexOfSliceList {

  def main(args: Array[String]) {
    val ls = (1 to 1000000).toList
    val seq = (30 to 73)
    var i = List[Int]()
    for (_ <- 1 to 10) i ::= (ls indexOfSlice seq)
  }

}
