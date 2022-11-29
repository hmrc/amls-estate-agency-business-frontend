import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28"            % "0.73.0",
    "uk.gov.hmrc"       %% "logback-json-logger"            % "5.1.0",
    "uk.gov.hmrc"       %% "govuk-template"                 % "5.69.0-play-28",
    "uk.gov.hmrc"       %% "play-ui"                        % "9.6.0-play-28",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"  % "1.9.0-play-28",
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-28"     % "5.11.0",
    compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.5" cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % "1.7.5" % Provided cross CrossVersion.full
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"          % "3.0.9",
    "org.scalatestplus.play"      %% "scalatestplus-play" % "5.0.0",
    "org.pegdown"                 %  "pegdown"            % "1.6.0",
    "org.jsoup"                   %  "jsoup"              % "1.14.2",
    "com.typesafe.play"           %% "play-test"          % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"        % "1.10.19",
    "org.scalacheck"              %% "scalacheck"         % "1.15.4"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
