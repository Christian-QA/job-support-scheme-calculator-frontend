//import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
//import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
//
//val appName = "job-support-scheme-calculator-frontend"
//
//val silencerVersion = "1.7.0"
//
//lazy val microservice = Project(appName, file("."))
//  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
//  .settings(
//    majorVersion                     := 0,
//    scalaVersion                     := "2.12.11",
//    libraryDependencies              ++= AppDependencies.compile ++ AppDependencies.test,
//    TwirlKeys.templateImports ++= Seq(
//      "uk.gov.hmrc.jobsupportschemecalculatorfrontend.config.AppConfig",
//      "uk.gov.hmrc.govukfrontend.views.html.components._",
//      "uk.gov.hmrc.govukfrontend.views.html.helpers._",
//      "uk.gov.hmrc.hmrcfrontend.views.html.components._"
//    ),
//    // ***************
//    // Use the silencer plugin to suppress warnings
//    // You may turn it on for `views` too to suppress warnings from unused imports in compiled twirl templates, but this will hide other warnings.
//    scalacOptions += "-P:silencer:pathFilters=routes",
//    libraryDependencies ++= Seq(
//      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
//      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
//    )
//    // ***************
//  )
//  .settings(publishingSettings: _*)
//  .configs(IntegrationTest)
//  .settings(integrationTestSettings(): _*)
//  .settings(resolvers += Resolver.jcenterRepo)


import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import wartremover.{Wart, wartremoverErrors, wartremoverExcluded}

val appName = "job-support-scheme-calculator-frontend"

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
addCommandAlias("fix", "all compile:scalafix test:scalafix")

resolvers += Resolver.bintrayRepo("hmrc", "releases")

lazy val wartremoverSettings =
  Seq(
    wartremoverErrors in (Compile, compile) ++= Warts.allBut(
      Wart.DefaultArguments,
      Wart.ImplicitConversion,
      Wart.ImplicitParameter,
      Wart.Nothing,
      Wart.Overloading,
      Wart.ToString
    ),
    wartremoverExcluded in (Compile, compile) ++=
      routes.in(Compile).value ++
        (baseDirectory.value ** "*.sc").get ++
        Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala"),
    wartremoverErrors in (Test, compile) --= Seq(Wart.Any, Wart.NonUnitStatements, Wart.Null, Wart.PublicInference)
  )

lazy val scoverageSettings =
  Seq(
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*Reverse.*;.*(config|testonly|views).*;.*(BuildInfo|Routes).*",
    ScoverageKeys.coverageMinimum := 80.00,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )

lazy val microservice = Project(appName, file("."))
  .enablePlugins(
    play.sbt.PlayScala,
    SbtAutoBuildPlugin,
    SbtGitVersioning,
    SbtDistributablesPlugin,
    SbtArtifactory
  )
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"))
  .settings(addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full))
  .settings(addCompilerPlugin("com.github.ghik" % "silencer-plugin" % "1.6.0" cross CrossVersion.full))
  .settings(addCompilerPlugin(scalafixSemanticdb))
  .settings(scalaVersion := "2.12.10")
  .settings(
    majorVersion := 0,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
  )
  .settings(routesImport := Seq.empty)
  .settings(TwirlKeys.templateImports := Seq.empty)
  .settings(
    scalacOptions ++= Seq(
      "-Yrangepos",
      "-language:postfixOps"
    ),
    scalacOptions in Test --= Seq("-Ywarn-value-discard")
  )
  .settings(publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(wartremoverSettings: _*)
  .settings(scoverageSettings: _*)
  .settings(PlayKeys.playDefaultPort := 7300)
  .settings(scalafmtOnCompile := true)
