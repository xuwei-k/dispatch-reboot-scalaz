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

    "applicative" in {
      import dispatch_scalaz.Applicative._
      checkApplicativeLaws[Promise,String]
    }
  }

  implicit def PromiseArbitrary[A](implicit a: Arbitrary[A]): Arbitrary[Promise[A]] =
    implicitly[Arbitrary[A]] map Promise.apply

  // https://github.com/scalaz/scalaz/blob/v6.0.4/tests/src/test/scala/scalaz/EqualTest.scala#L111-118
  def checkEqualLaws[A: Equal : Manifest : Arbitrary]: Unit = {
    val typeName = manifest[A].toString
    typeName in {
      import ScalazProperties.Equal._
      commutativity[A] must pass
      identity[A] must pass
    }
  }

  // https://github.com/scalaz/scalaz/blob/v6.0.4/tests/src/test/scala/scalaz/FunctorTest.scala#L97-109
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

  // https://github.com/scalaz/scalaz/blob/v6.0.4/tests/src/test/scala/scalaz/ApplicativeTest.scala#L29-46
  def checkApplicativeLaws[M[_], A](implicit mm: Applicative[M],
                                    ea: Equal[A],
                                    man: Manifest[M[A]],
                                    ema: Equal[M[A]],
                                    arbma: Arbitrary[M[A]],
                                    arba: Arbitrary[A]): Unit = {
    val typeName = man.toString
    implicit val arbMAA: Arbitrary[M[A => A]] = ((a: A) => a).pure[M].pure[Arbitrary]
    typeName in {
      import ScalazProperties.Applicative._
      identity[M, A] must pass
      composition[M, A, A, A] must pass

      // TODO These don't terminate. Investigate.
//      homomorphism[M, A, A] must pass
//      interchange[M, A, A] must pass
    }
  }
}

