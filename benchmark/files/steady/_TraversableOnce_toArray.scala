object TraversableOnce_toArray {

  def main(args: Array[String]) {
    val seq = Seq(1 to 1000000: _*)
    var ls = List[Array[Int]]()
    var i = 0
    while (i < 10) {
      ls ::= seq.toArray
      i += 1
    }
    ls foreach (_ => ())
  }

}