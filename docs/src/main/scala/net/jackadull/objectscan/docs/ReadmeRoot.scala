package net.jackadull.objectscan.docs

import net.jackadull.jackadocs.structure.badges.BadgeGenerators
import net.jackadull.jackadocs.structure.{Chapter, DocsMetaData, RootChapter}
import net.jackadull.objectscan.docs.readme.{Ch1_DependencyManagementAndCompatiblity, Ch2_UseCases, Ch3_Usage}

import scala.xml.NodeSeq

object ReadmeRoot extends RootChapter with BadgeGenerators {
  override def title:NodeSeq = "Objectscan"

  override def contentsBeforeTOC(root:RootChapter):NodeSeq =
<p>{travisCIBadge} {mavenCentralBadge} {codeFactorBadge} {snykBadge}</p>

  override def contents(root:RootChapter):NodeSeq =
<p>
  A small utility library for quickly finding all singleton instances of a given type, including subtypes.
</p>
<p>
  Internally, it uses <a href="https://github.com/classgraph/classgraph"><tt>ClassGraph</tt></a> for performing
   top-speed classpath scans.
  However, this is an implementation detail which may change in future versions, or it may not change.
  Users of Objectscan should only use the simple interface which it exposes to the outside world, and not rely on any
   implementation specifics.
</p>

  override def subChapters:Seq[Chapter] = Seq(Ch1_DependencyManagementAndCompatiblity, Ch2_UseCases, Ch3_Usage)

  override def docsMetaData:DocsMetaData = Main
}
