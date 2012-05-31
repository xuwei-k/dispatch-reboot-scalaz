import scalaz._,Scalaz._
import dispatch.Promise

package object dispatch_scalaz extends Instances

package dispatch_scalaz{
  trait Instances{

    implicit val PromisePure:Pure[Promise] = new Pure[Promise]{
      def pure[A](a: => A) = Promise(a)
    }

    implicit val PromiseFunctor:Functor[Promise] = new Functor[Promise]{
      def fmap[A, B](t: Promise[A], f: A => B): Promise[B] = t map f
    }

    implicit val PromiseApply:Apply[Promise] = Scalaz.FunctorBindApply[Promise]

    implicit def PromiseBind: Bind[Promise] = new Bind[Promise] {
      def bind[A, B](r: Promise[A], f: A => Promise[B]) = r flatMap f
    }

    implicit def PromiseEach: Each[Promise] = new Each[Promise] {
      def each[A](e: Promise[A], f: A => Unit) = f(e.apply)
    }

    implicit def PromiseEqual[A: Equal]:Equal[Promise[A]] =
      Scalaz.equal[Promise[A]]((a1, a2) => a1.apply === a2.apply )

    implicit val PromiseTraverse:Traverse[Promise] = new Traverse[Promise] {
      def traverse[F[_] : Applicative, A, B](f: A => F[B], ta: Promise[A]): F[Promise[B]] =
        f(ta.apply) map (Promise(_: B))
    }

    implicit val PromiseCojoin:Cojoin[Promise] = new Cojoin[Promise] {
      def cojoin[A](a: Promise[A]) = Promise(a)
    }

    implicit val PromiseCopure:Copure[Promise] = new Copure[Promise] {
      def copure[A](a: Promise[A]) = a.apply
    }
  }
}
