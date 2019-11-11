package net.jackadull.objectscan.docs

import net.jackadull.jackadocs.execution.{JackadocsMain, JackadocsProjectInfo}
import net.jackadull.objectscan.ObjectscanInfo

object Main extends App with JackadocsMain {
  jackadocs.generateAt(s"$projectDir/docs/README.md").markdownFor(ReadmeRoot)

  override def organizationName = "jackadull"
  override def projectDir:String = "."
  override lazy val projectInfo = new JackadocsProjectInfo {
    override def groupID:String = ObjectscanInfo.organization
    override def artifactID:String = ObjectscanInfo.name
    override def version:String = ObjectscanInfo.version
  }
  def sourceRepoProvider = "github"
}
