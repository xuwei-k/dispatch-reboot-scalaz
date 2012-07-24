import sbt._,Keys._

object build extends Build{

  val scalazV = "6.0.4"

  lazy val root = Project(
    "dispatch-reboot-scalaz",
    file(".")
  )settings(
    scalaVersion := "2.9.2",
    resolvers ++= Seq(
      "https://oss.sonatype.org/content/repositories/releases"
    ).map{u => u at u},
    libraryDependencies ++= Seq(
      "net.databinder.dispatch" %% "dispatch-core" % "0.9.4",
      "org.scalaz" %% "scalaz-core" % scalazV,
      "org.scalaz" %% "scalaz-scalacheck-binding" % scalazV % "test",
      "org.scala-tools.testing" % "specs_2.9.0-1" % "1.6.8" % "test"
    )
  ) dependsOn (
//    scalazTests % "test->test"
  )

  // TODO
  // I want to reuse following methods
  // https://github.com/scalaz/scalaz/blob/v6.0.4/tests/src/test/scala/scalaz/FunctorTest.scala#L97-109
  // https://github.com/scalaz/scalaz/blob/v6.0.4/tests/src/test/scala/scalaz/EqualTest.scala#L111-118
  // but `git clone` too slow ... :-(
  lazy val scalazTests = ProjectRef(
    uri("git://github.com/scalaz/scalaz.git#v"+scalazV),"tests"
  )

}
