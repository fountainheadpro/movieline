name := """searcher"""

EclipseKeys.withSource := true

version := "1.0"

scalaVersion := "2.10.2"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "spray nightlies" at "http://nightlies.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"            % "2.2.0",
  "com.typesafe.akka"      %% "akka-slf4j"            % "2.2.0",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",  
  "org.apache.lucene" % "lucene-core" % "4.7.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "4.8.0",
  "org.apache.lucene" % "lucene-queryparser" % "4.8.0",
  "org.apache.lucene" % "lucene-suggest" % "4.8.0",  
  "org.scala-lang"          % "scala-reflect"         % "2.10.2",
  "org.specs2"             %% "specs2"                % "1.14"         % "test",
  "com.typesafe.akka"      %% "akka-testkit"          % "2.2.0"        % "test",
  "com.novocode"            % "junit-interface"       % "0.7"          % "test->default"
)

resolvers ++= Seq(
  "Sonatype.org Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype.org Releases" at "http://oss.sonatype.org/service/local/staging/deploy/maven2"
)



scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

parallelExecution in Test := false

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
