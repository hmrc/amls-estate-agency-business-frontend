import sbt._

object AppDependencies {

  val bootstrapVersion = "8.4.0"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-28"     % bootstrapVersion,
    "uk.gov.hmrc"       %% "play-conditional-form-mapping"  % "1.13.0-play-28",
    "uk.gov.hmrc"       %% "play-frontend-hmrc"             % "7.29.0-play-28",
    "commons-codec"     %  "commons-codec"                  % "1.16.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                 %% "bootstrap-test-play-28"  % bootstrapVersion,
    "org.scalatestplus"           %% "scalacheck-1-17"         % "3.2.17.0",
    "org.mockito"                 %% "mockito-scala-scalatest" % "1.17.30",
    "com.vladsch.flexmark"        % "flexmark-all"             % "0.62.2"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
