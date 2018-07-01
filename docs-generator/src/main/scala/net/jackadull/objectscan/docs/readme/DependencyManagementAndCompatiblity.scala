package net.jackadull.objectscan.docs.readme

import net.jackadull.jackadocs.structure.Chapter
import net.jackadull.objectscan.ObjectscanInfo

import scala.language.postfixOps

object DependencyManagementAndCompatiblity extends Chapter {
  def id = "dependency_management_and_compatibility"
  def title = "Dependency Management and Compatibility"

  def contents =
<p>
  Objectscan is compatible with Scala 2.12.
  Best effort is made to always keep it up-to-date with the latest Scala version.
</p>
<p>
  Cross-versioning will not be supported.
  When a new Scala version is released, your code should be updated to that version as soon as possible anyways.
</p>

  override def subChapters = Seq(
    Chapter("sbt", "SBT",
<pre><code class="language-scala">
libraryDependencies += "net.jackadull" %% "objectscan" % "{ObjectscanInfo version}"
</code></pre>
    ),
    Chapter("maven", "Maven",
<pre><code class="language-xml">
{<dependency>
  <groupId>net.jackadull</groupId>
  <artifactId>objectscan_2.12</artifactId>
  <version>{ObjectscanInfo version}</version>
</dependency> toString}
</code></pre>
    )
  )
}
