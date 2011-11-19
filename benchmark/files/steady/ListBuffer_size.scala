object ListBuffer_size {

  def main(args: Array[String]) {
    val lb = collection.mutable.ListBuffer((0 to 1000000): _*)
    val ls = List(lb.size)
    ls foreach (_ => ())
  }

}
