package net.jackadull.objectscan.docs.readme

import net.jackadull.jackadocs.structure.{Chapter, RootChapter}

import scala.xml.NodeSeq

object Ch2_UseCases extends Chapter {
  override def title:NodeSeq = "Use cases"

  override def contents(root:RootChapter):NodeSeq =
<p>
  Use of this utility library is somewhat focused on a certain use case:
  It will find all singleton instances of a certain type, optionally with certain restrictions.
  This is achieved by scanning the classpath.
</p>
<p>
  This can be very useful for cases where it is necessary to enumerate all instances of a certain type, for example for
   enumeration-like types.
  When used this way, this can spare manually keeping a list of all instances in the source code, and automate the
   process of finding all instances at runtime, with only little overhead.
  This reduces the need of code maintenance, and the chance of making mistakes.
</p>
<p>
  Another possible use case is for projects that are just static text generators.
  In such projects, a certain trait can mark a chapter, or a web page.
  The scanner can then be used for finding all chapters or pages dynamically, potentially rendering them.
  In this way, new elements can be created simply by creating a singleton, without the need to register them anywhere.
</p>

  override def subChapters:Seq[Chapter] = Seq(
    Chapter("How Singletons are provided and found",
<p>
  Objectscan finds singleton instances.
  By "singleton", this document mostly means a typical Scala singleton, i.e. a Scala <tt>object</tt>.
</p>
<p>
  However, not all singletons can be conveniently defined as <tt>object</tt>.
  Sometimes, it can be convenient to specify an algorithm that generates a number of instances.
</p>
<p>
  For such cases, the <tt>ObjectScanSource</tt> trait may be implemented:
</p>
<pre><code class="language-scala">
trait TrafficLightColor {{def name:String}}

object TrafficLightColor extends ObjectScanSource {{
  def scannableObjects = Seq("red", "green", "yellow").map(color => new TrafficLightColor {{def name = color}})
}}
</code></pre>
<p>
  This will simply inject the returned <tt>scannableObjects</tt> into the pool of scannable singletons, and behave as if
   they where defined as Scala <tt>object</tt>, each respectively.
  When a scan is restricted by package prefix, the package prefix of the enclosing <tt>ObjectScanSource</tt> counts for
   that restriction.
</p>
<p>
  Note that it is possible to use Objectscan inside a <tt>scannableObjects</tt> implementation.
  However, such cases must be handled with great care:
  Objectscan does not handle recursions gracefully.
  The developer must make sure that no recursions can occur on a path of <tt>ObjectScanSources</tt> that are linked to
   each other.
</p>
<p>
  Also take into account that <tt>scannableObjects</tt> <b>must always</b> return the same result, no matter when or how
   many times it gets called.
  The reason is given below, under the heading "Stability assumption".
</p>
    ),
    Chapter("Not to be used for DI",
<p>
  However, Objectscan is not useful for dependency injection.
  It offers no way for conflict resolution or scope management.
</p>
<p>
  For example:
  Suppose you define a trait <tt>DatabaseAccess</tt>, which is an abstract model for accessing the database.
  Instead of hard-wiring to a static instance, you use Objectscan for finding the single instance of
   <tt>DatabaseAccess</tt>, and then later on, to use that instance.
  One module defines a <tt>MySQLDatabaseAccess</tt> singleton, which is found correctly.
  So far, everything looks fine.
</p>
<p>
  But then, another module defines <tt>MongoDBDatabaseAccess</tt>, because it stores reporting data in a Mongo DB.
  All of a sudden, those two modules cannot be used together, because they introduce two <tt>DatabaseAccess</tt>
   singletons to the classpath.
</p>
<p>
  Even though this case could in theory be resolved, by adding your own discriminator methods for resolving scope, there
   are more arguments why Objectscan is not suited for DI:
</p>
<ul>
  <li>
    <p>
      It is not very good at error handling. If your database connection cannot be established, the error will be hard
       to trace.
    </p>
  </li>
  <li>
    <p>
      It does not handle the case of circular dependencies.
    </p>
  </li>
  <li>
    <p>
      It does not offer any configuration options, for example, for injecting your database password from a secure
       source.
    </p>
  </li>
</ul>
    )
  )
}
