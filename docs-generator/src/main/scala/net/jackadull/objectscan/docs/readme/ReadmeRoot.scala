package net.jackadull.objectscan.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.language.postfixOps

object ReadmeRoot extends Chapter {
  def id = "objectscan"
  def title = "Objectscan"

  def contents =
<p>
  A small utility library for quickly finding all singleton instances of a given type, including subtypes.
</p>
<p>
  Internally, it uses <a href="https://github.com/lukehutch/fast-classpath-scanner"><tt>FastClasspathScanner</tt></a> for performing top-speed classpath scans.
  However, this is an implementation detail which may change in future versions, or it may not change.
  Users of Objectscan should only use the simple interface which it exposes to the outside world, and not rely on any implementation specifics.
</p>

  override def subChapters:Seq[Chapter] = Seq(DependencyManagementAndCompatiblity, UseCases, Usage)
}
