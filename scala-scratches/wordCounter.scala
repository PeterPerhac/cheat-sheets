import scala.util.Try

trait Monoid[A] {
  def op(a1: A, a2: A): A
  val zero: A
}


object Monoid {

  def foldMapV[A, B](as: IndexedSeq[A], m: Monoid[B])(f: A => B): B =
    as.size match {
      case 0 => m.zero
      case 1 => f(as(0))
      case n => val (fh, sh) = as splitAt (n / 2)
      m.op(foldMapV(fh, m)(f), foldMapV(sh,m)(f))
    }

}

sealed trait WC
final case class Stub(chars: String) extends WC
final case class Part(lStub: String, words: Int, rStub: String) extends WC

object App {

  implicit object WordCountMonoid extends Monoid[WC] {
    val zero = Stub("")

    def op(a: WC, b: WC) = (a, b) match {
      case (Stub(c), Stub(d)) => Stub(c + d)
      case (Stub(c), Part(l, w, r)) => Part(c + l, w, r)
      case (Part(l, w, r), Stub(c)) => Part(l, w, r + c)
      case (Part(l1, w1, r1), Part(l2, w2, r2)) =>
        Part(l1, w1 + (if ((r1 + l2).isEmpty) 0 else 1) + w2, r2)
    }
  }

  def count(s: String)(implicit wcm: Monoid[WC]): Int = {
    def wc(c: Char): WC =
      if (c.isWhitespace)
        Part("", 0, "")
      else
        Stub(c.toString)
        def unstub(s: String) = s.length min 1
        Monoid.foldMapV(s.toIndexedSeq, wcm)(wc) match {
          case Stub(s) => unstub(s)
          case Part(l, w, r) => unstub(l) + w + unstub(r)
        }
  }


  def main(args:Array[String]):Unit = {
    for {
      filename <- args.headOption
      text <- Try{
          scala.io.Source.fromFile(filename).getLines.mkString(" ")
        }.toOption
      } println(s"Counted ${count(text)} words in file $filename")
  }

}
