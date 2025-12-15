import sbt._

object AppDependencies {

  val bootstrapVersion = "9.19.0"
  val playVersion      = "play-30"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"  %% s"bootstrap-frontend-$playVersion"            % bootstrapVersion,
    "uk.gov.hmrc"  %% s"play-conditional-form-mapping-$playVersion" % "3.4.0",
    "uk.gov.hmrc"  %% s"play-frontend-hmrc-$playVersion"            % "12.22.0",
    "commons-codec" % "commons-codec"                               % "1.17.2"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-test-$playVersion" % bootstrapVersion,
    "org.scalatestplus" %% "scalacheck-1-17"              % "3.2.18.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
