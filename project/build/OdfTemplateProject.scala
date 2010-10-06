import sbt._
import de.element34.sbteclipsify._

/**
 * Project definition. The library dependencies are stated here.
 */
class OdfTemplateProject(info: ProjectInfo) extends DefaultProject(info) with Eclipsify {
  
  /**
   * Repository needed for ScalaTest
   */
  val scalaToolsReleases = "Scala-Tools Maven2 Releases Repository" at 
        "http://www.scala-tools.org/repo-releases/"
  
  /**
   * Scala Test Framework.
   */
  val scalaTest = "org.scalatest" % "scalatest" % "1.2" % "test"

  /**
   * XML Parser, needed for Open Document parsing.
   */
  val xerces = "xerces" % "xercesImpl" % "2.9.1"

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
  
  /**
   * Also publish sources and tests.
   */
  override def packageSrcJar= defaultJarPath("-sources.jar")
  val sourceArtifact = Artifact.sources(artifactID)
  val testArtifact = Artifact(artifactID, "test")
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc, packageTest)
}
