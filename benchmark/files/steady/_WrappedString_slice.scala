object WrappedString_slice {

  def main(args: Array[String]) {
    val str = "asdf" * 100
    val ls = for (i <- 1 to 400000) yield (str.slice(i % 50000, 300))
    ls foreach (_ => ())
  }

}
