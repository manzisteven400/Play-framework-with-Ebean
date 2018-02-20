import com.github.play2war.plugin._

name := "university"

version := "1.0"

libraryDependencies ++= Seq(
  javaJdbc,
  javaCore,
  javaEbean,
  cache,
  filters,
  "mysql" % "mysql-connector-java" % "5.1.27",
  "commons-io" % "commons-io" % "2.3",
  "commons-codec" % "commons-codec" % "1.8",
  "com.typesafe" %% "play-plugins-mailer" % "2.2.0",
  "com.typesafe.play" %% "play-mailer" % "2.4.0-RC1",
  "org.apache.poi" % "poi" % "3.8",
  "org.apache.poi" % "poi-ooxml" % "3.9",
  "org.apache.httpcomponents" % "httpmime" % "4.2.3",
  "org.imgscalr" % "imgscalr-lib" % "4.2",
  "org.springframework" % "spring-tx" % "4.3.5.RELEASE",
  "javax.mail" % "mail" % "1.4.7",
  "org.mindrot" % "jbcrypt" % "0.3m"
)     

play.Project.playJavaSettings

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"
