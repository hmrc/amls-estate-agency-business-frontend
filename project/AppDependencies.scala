import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-28"     % "7.13.0",
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"  % "1.13.0-play-28",
    "uk.gov.hmrc"       %% "play-frontend-hmrc"             % "6.6.0-play-28",
    "commons-codec"     %  "commons-codec"                  % "1.15",
    compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.17.13" cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % "1.17.13" % Provided cross CrossVersion.full,

  )

  val test = Seq(
    "uk.gov.hmrc"                 %% "bootstrap-test-play-28"  % "7.13.0",
    "org.scalatest"               %% "scalatest"               % "3.0.9",
    "org.scalatestplus"           %% "scalacheck-1-17"         % "3.2.16.0",
    "org.mockito"                 %% "mockito-scala-scalatest" % "1.17.12",
    "org.scalacheck"              %% "scalacheck"              % "1.15.4"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
