package net.jackadull.objectscan.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}
import net.jackadull.objectscan.ObjectscanInfo

import scala.xml.NodeSeq

object Ch1_DependencyManagementAndCompatiblity extends Chapter {
  override def title:NodeSeq = "Dependency management and compatibility"

  override def contents(root:RootChapter):NodeSeq =
<p>
  Objectscan is compatible with Scala {ObjectscanInfo.scalaBinaryVersion}.
  Best effort is made to always keep it up-to-date with the latest Scala version.
</p>
<p>
  Cross-versioning will not be supported.
  When a new Scala version is released, your code should be updated to that version as soon as possible anyways.
</p>

  override def subChapters:Seq[Chapter] = Seq(
    Chapter("SBT",
<pre><code class="language-scala">
libraryDependencies += "{ObjectscanInfo.organization}" %% "{ObjectscanInfo.name}" % "{ObjectscanInfo.version}"
</code></pre>
    ),
    Chapter("Maven",
<pre><code class="language-xml">
{<dependency>
  <groupId>{ObjectscanInfo.organization}</groupId>
  <artifactId>{ObjectscanInfo.name}_{ObjectscanInfo.scalaBinaryVersion}</artifactId>
  <version>{ObjectscanInfo.version}</version>
</dependency>.toString}
</code></pre>
    )
  )
}
