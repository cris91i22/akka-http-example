version := "1.0-SNAPSHOT"

scalaVersion := "2.12.1"

lazy val root =
  project.in( file("."))
    .settings(name := "akka-hhtp-example")
      .settings(libraryDependencies ++= Seq(
        "com.typesafe.akka"           %% "akka-actor"       % "2.4.17",
        "com.typesafe.akka"           %% "akka-http"        % "10.0.3",
        "de.heikoseeberger"           %% "akka-http-json4s" % "1.12.0",
        "org.json4s"                  %% "json4s-jackson"   % "3.5.0",
        "org.json4s"                  %% "json4s-ext"       % "3.5.0",
        "com.typesafe.slick"          %% "slick"            % "3.2.0-M2",
        "com.github.tminglei"         %% "slick-pg"         % "0.15.0-M4",
        "com.typesafe.slick"          %% "slick-hikaricp"   % "3.2.0-M2",
        "com.zaxxer"          % "HikariCP"      % "2.6.0",
        "org.postgresql"      % "postgresql"    % "9.4.1208.jre7",
        "joda-time"           % "joda-time"     % "2.7"
      ))
