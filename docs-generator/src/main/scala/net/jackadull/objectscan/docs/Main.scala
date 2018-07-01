package net.jackadull.objectscan.docs

import net.jackadull.jackadocs.execution.{Jackadocs, JackadocsMain, JackadocsProjectInfo}
import net.jackadull.objectscan.ObjectscanInfo
import net.jackadull.objectscan.docs.readme.ReadmeRoot

import scala.language.postfixOps

object Main extends App with JackadocsMain {
  jackadocs generateAt s"$projectDir/README.md" markdownFor ReadmeRoot

  def projectDir = ".."
  lazy val projectInfo = new JackadocsProjectInfo with ObjectscanInfo
  def organizationName = "jackadull"
  def sourceRepoProvider = "github"
}
