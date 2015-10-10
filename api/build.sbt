organization  := "io.buildo"

version       := "SNAPSHOT"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked",
                     "-deprecation",
                     "-feature",
                     "-encoding", "utf8",
                     "-language:postfixOps",
                     "-language:implicitConversions",
                     "-language:higherKinds",
                     "-language:reflectiveCalls")

fork := true

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "buildo mvn" at "https://raw.github.com/buildo/mvn/master/releases"
)

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "io.buildo"           %%  "ingredients-jsend" % "0.3",
    "io.buildo"           %%  "nozzle"          % "0.5.0",
    "io.spray"            %%  "spray-testkit"   % sprayV  % "test",
    "io.spray"            %%  "spray-json"      % "1.3.1",
    "org.scalatest"       %%  "scalatest"       % "2.2.0" % "test",
    "org.scalamock"       %%  "scalamock-scalatest-support" % "3.2" % "test",
    "com.typesafe.slick"  %%  "slick"           % "2.1.0",
    "mysql"               %   "mysql-connector-java" % "5.1.25"
  )
}
