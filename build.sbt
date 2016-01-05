// set the name of the project
name := "odftemplate"

version := "1.0"

organization := "de.gtlk"

// add compile dependencies on some dispatch modules
{
  val liftVersion = "2.3"
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "xerces" % "xercesImpl" % "2.9.1"
  )
}

/**
 * ODF Toolkit does not have a working maven repo - 
 * therefore is included in the lib folder.
 * Current Versions can be downloaded from
 * http://odftoolkit.org/projects/odfdom/downloads.
 * 
 * There is a Maven Repository under 
 * https://svn.odftoolkit.org/svn/odfdom~maven2/release
 * but that uses a non-secure SSL certificate, which is the
 * reason why it can't be used directly as a repository here.
 * 
 * In projects that use OdfTemplate, the odfdom-jar must thus
 * be included in the projects directly.
 */
// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

// set the Scala version used for the project
scalaVersion := "2.11.7"

// add a sequence of maven-style repositories
resolvers ++= Seq("Scala-Tools Maven2 Releases Repository" at 
    "http://www.scala-tools.org/repo-releases/")
