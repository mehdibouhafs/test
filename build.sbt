name := """test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "org.springframework.retry" % "spring-retry" % "1.1.2.RELEASE",
"org.springframework" % "spring-context" % "4.2.4.RELEASE",
"org.springframework" % "spring-core" % "4.2.4.RELEASE",
"org.springframework" % "spring-beans" % "4.2.4.RELEASE",
"org.springframework" % "spring-aop" % "4.2.4.RELEASE",
"org.springframework.batch" % "spring-batch-core" % "3.0.7.RELEASE",
"org.springframework" % "spring-jdbc" % "4.2.4.RELEASE",
"mysql" % "mysql-connector-java" % "5.1.38",
"org.springframework" % "spring-oxm" % "4.2.6.RELEASE",
"com.sun.xml.bind" % "jaxb-impl" % "2.2.11",
"com.sun.xml.bind" % "jaxb-core" % "2.2.11",
"commons-io" % "commons-io" % "2.4"

)

