package dispatch_scalaz

import org.specs.Specification
import org.specs.ScalaCheck
import org.scalacheck.{Arbitrary, Prop}
import scalaz._,Scalaz._
import scalacheck.ScalazProperties
import scalacheck.ScalaCheckBinding._
import scalacheck.ScalazArbitrary._
import dispatch.Promise

class Test extends Specification with ScalaCheck{

  "dispatch scalaz" should{
    checkEqualLaws[Promise[String]]
    checkFunctorLaws[Promise,Int]
  }

  implicit def PromiseArbitrary[A](implicit a: Arbitrary[A]): Arbitrary[Promise[A]] =
    implicitly[Arbitrary[A]] map Promise.apply

  def checkEqualLaws[A: Equal : Manifest : Arbitrary]: Unit = {
    val typeName = manifest[A].toString
    typeName in {
      import ScalazProperties.Equal._
      commutativity[A] must pass
      identity[A] must pass
    }
  }

  def checkFunctorLaws[F[_], A](implicit mm: Functor[F],
                                ea: Equal[A],
                                man: Manifest[F[A]],
                                ema: Equal[F[A]],
                                arbma: Arbitrary[F[A]],
                                arba: Arbitrary[A]): Unit = {
    val typeName = man.toString
    typeName in {
      import ScalazProperties.Functor._
      identity[F, A] must pass
      associative[F, A, A, A] must pass
    }
  }
}

