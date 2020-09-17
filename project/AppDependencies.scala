import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo" %% "play2-reactivemongo"            % "0.20.3-play26",
    "uk.gov.hmrc"       %% "logback-json-logger"            % "4.8.0",
    "uk.gov.hmrc"       %% "govuk-template"                 % "5.55.0-play-26",
    "uk.gov.hmrc"       %% "play-health"                    % "3.15.0-play-26",
    "uk.gov.hmrc"       %% "play-ui"                        % "8.12.0-play-26",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"  % "1.3.0-play-26",
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-26"     % "2.25.0",
    "uk.gov.hmrc"       %% "play-whitelist-filter"          % "3.4.0-play-26",
    "uk.gov.hmrc"       %% "auth-client"                    % "3.0.0-play-26"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"          % "3.0.9",
    "org.scalatestplus.play"      %% "scalatestplus-play" % "3.1.2",
    "org.pegdown"                 %  "pegdown"            % "1.6.0",
    "org.jsoup"                   %  "jsoup"              % "1.13.1",
    "com.typesafe.play"           %% "play-test"          % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"        % "1.10.19",
    "org.scalacheck"              %% "scalacheck"         % "1.14.3"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
