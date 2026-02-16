import sbt._

object AppDependencies {

  val bootstrapVersion = "10.5.0"
  val playVersion      = "play-30"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"  %% s"bootstrap-frontend-$playVersion"            % bootstrapVersion exclude("org.apache.commons", "commons-lang3"),
    "uk.gov.hmrc"  %% s"play-conditional-form-mapping-$playVersion" % "3.4.0",
    "uk.gov.hmrc"  %% s"play-frontend-hmrc-$playVersion"            % "12.31.0",
    "commons-codec" % "commons-codec"                               % "1.17.2",
    "org.apache.commons"   % "commons-lang3"               % "3.18.0",
    "ch.qos.logback"       % "logback-core"                % "1.5.27",
    "ch.qos.logback"       % "logback-classic"             % "1.5.27",
    "at.yawk.lz4"          %  "lz4-java"                   % "1.10.3"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-test-$playVersion" % bootstrapVersion,
    "org.scalatestplus" %% "scalacheck-1-17"              % "3.2.18.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
