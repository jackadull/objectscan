package net.jackadull.objectscan.docs

import net.jackadull.jackadocs.execution.Jackadocs
import net.jackadull.objectscan.ObjectscanInfo
import net.jackadull.objectscan.docs.readme.ReadmeRoot

import scala.language.postfixOps

object Main extends App {
  val jackadocs = Jackadocs fromArgs args

  jackadocs.requirePOMVersion("../pom.xml")(ObjectscanInfo Version)

  jackadocs generateAt "../README.md" markdownFor ReadmeRoot
}
