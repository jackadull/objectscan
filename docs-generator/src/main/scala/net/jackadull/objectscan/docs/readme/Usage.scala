package net.jackadull.objectscan.docs.readme

import net.jackadull.jackadocs.structure.Chapter

import scala.xml.NodeSeq

object Usage extends Chapter {
  def id = "usage"
  def title = "Usage"

  def contents:NodeSeq =
<p>
  <tt>objectscan</tt> relies on the assumption that all scans yield the same result during the JVM lifetime, no matter at what time they are called.
  This also implies that all scan results can be cached, as future calls with the same parameters would return the same results anyway.
</p>
<p>
  The stability assumption can in theory be violated (by implementing <tt>ObjectScanSource</tt> in an unstable way), which is strongly advised against.
  This would lead to inconsistent behavior of the application.
</p>

  override def subChapters:Seq[Chapter] = Seq(
    Chapter("basic_usage", "Basic Usage", NodeSeq.Empty, subChapters = Seq(
      Chapter("creating_an_instance_of_all", "Creating an Instance of All",
<p>
  The main entry point for all code using <tt>objectscan</tt> is <tt>net.jackadull.objectscan.All</tt>.
  Creating an instance of <tt>All</tt> does not yet initiate any scanning operation.
  It is just a preparation for upcoming scans.
</p>
<p>
  Probably, you may want to reuse your <tt>All</tt> instance, because it may cache its results.
</p>
<p>
  At first, an instance of <tt>All</tt> must be created with some limiting factor, or otherwise classpath scans of bigger projects can become very inefficient.
  Usually, you can either limit the package prefix of the scanned objects, or determine that they must be nested inside another singleton.
</p>
<pre><code class="language-scala">
import net.jackadull.objectscan.All
val all = All withinPackagePrefixes Seq("net.jackadull.example", "com.mycompany")
</code></pre>
<p>
  The above example will scan all class files whose package begins with either <tt>net.jackadull.exampe</tt> or <tt>com.mycompany</tt>.
</p>
<pre><code class="language-scala">
object MyEnclosingSingleton {
  // insert scannable singletons here
}
val all = All nestedInside MyEnclosingSingleton
</code></pre>
<p>
  This will only scan all class files that are nested inside <tt>MyEnclosingSingleton</tt>.
</p>
<p>
  <b>Note:</b> If you really want to scan <i>the whole</i> classpath, use <tt>All withinPackagePrefixes Seq()</tt>.
  Use at your own risk though, as these scans grow proportionally with classpath size.
</p>
      ),
      Chapter("running_scans", "Running Scans",
<p>
  Here are some examples for classpath scans:
</p>
<pre><code class="language-scala">
val all = ??? // see above
all.of[SomeType].toSeq               // returns Seq[SomeType]
all.of[SomeType].where(_.isActive)   // returns a filtered Seq[SomeType]
all.of[SomeType].isEmpty             // true if there are no instances of SomeType
all.of[SomeType].nonEmpty            // true if there is at least one singleton instance of SomeType
</code></pre>
<p>
  The examples above give a good overview over the possible operations on <tt>All</tt> instances.
</p>
      )
    ))
  )
}
