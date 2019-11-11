# Objectscan
[![Travis CI](https\:\/\/travis\-ci\.org\/jackadull\/objectscan\.svg)](https\:\/\/travis\-ci\.org\/jackadull\/objectscan) [![Maven Central](https\:\/\/img\.shields\.io\/maven\-central\/v\/net\.jackadull\/objectscan\.svg)](https\:\/\/search\.maven\.org\/\#search\%7Cga\%7C1\%7Cg\%3A\%22net\.jackadull\%22\%20AND\%20a\%3A\%22objectscan\%22) [![Codefactor](https\:\/\/www\.codefactor\.io\/repository\/github\/jackadull\/objectscan\/badge)](https\:\/\/www\.codefactor\.io\/repository\/github\/jackadull\/objectscan) [![Snyk](https\:\/\/snyk\.io\/test\/github\/jackadull\/objectscan\/badge\.svg)](https\:\/\/snyk\.io\/test\/github\/jackadull\/objectscan)

A small utility library for quickly finding all singleton instances of a given type\, including subtypes\.

Internally\, it uses [`ClassGraph`](https\:\/\/github\.com\/classgraph\/classgraph) for performing top\-speed classpath scans\. However\, this is an implementation detail which may change in future versions\, or it may not change\. Users of Objectscan should only use the simple interface which it exposes to the outside world\, and not rely on any implementation specifics\.

## Dependency management and compatibility
Objectscan is compatible with Scala 2\.13 \. Best effort is made to always keep it up\-to\-date with the latest Scala version\.

Cross\-versioning will not be supported\. When a new Scala version is released\, your code should be updated to that version as soon as possible anyways\.

### SBT
```scala
libraryDependencies += "net.jackadull" %% "objectscan" % "0.4.0-SNAPSHOT"
```
### Maven
```xml
<dependency>
  <groupId>net.jackadull</groupId>
  <artifactId>objectscan_2.13</artifactId>
  <version>0.4.0-SNAPSHOT</version>
</dependency>
```
## Use cases
Use of this utility library is somewhat focused on a certain use case\: It will find all singleton instances of a certain type\, optionally with certain restrictions\. This is achieved by scanning the classpath\.

This can be very useful for cases where it is necessary to enumerate all instances of a certain type\, for example for enumeration\-like types\. When used this way\, this can spare manually keeping a list of all instances in the source code\, and automate the process of finding all instances at runtime\, with only little overhead\. This reduces the need of code maintenance\, and the chance of making mistakes\.

Another possible use case is for projects that are just static text generators\. In such projects\, a certain trait can mark a chapter\, or a web page\. The scanner can then be used for finding all chapters or pages dynamically\, potentially rendering them\. In this way\, new elements can be created simply by creating a singleton\, without the need to register them anywhere\.

### How Singletons are provided and found
Objectscan finds singleton instances\. By \"singleton\"\, this document mostly means a typical Scala singleton\, i\.e\. a Scala `object` \.

However\, not all singletons can be conveniently defined as `object` \. Sometimes\, it can be convenient to specify an algorithm that generates a number of instances\.

For such cases\, the `ObjectScanSource` trait may be implemented\:

```scala
trait TrafficLightColor {def name:String}
object TrafficLightColor extends ObjectScanSource {
  def scannableObjects = Seq("red", "green", "yellow").map(color => new TrafficLightColor {def name = color})
}
```
This will simply inject the returned `scannableObjects` into the pool of scannable singletons\, and behave as if they where defined as Scala `object` \, each respectively\. When a scan is restricted by package prefix\, the package prefix of the enclosing `ObjectScanSource` counts for that restriction\.

Note that it is possible to use Objectscan inside a `scannableObjects` implementation\. However\, such cases must be handled with great care\: Objectscan does not handle recursions gracefully\. The developer must make sure that no recursions can occur on a path of `ObjectScanSources` that are linked to each other\.

Also take into account that `scannableObjects` must always return the same result\, no matter when or how many times it gets called\. The reason is given below\, under the heading \"Stability assumption\"\.

### Not to be used for DI
However\, Objectscan is not useful for dependency injection\. It offers no way for conflict resolution or scope management\.

For example\: Suppose you define a trait `DatabaseAccess` \, which is an abstract model for accessing the database\. Instead of hard\-wiring to a static instance\, you use Objectscan for finding the single instance of `DatabaseAccess` \, and then later on\, to use that instance\. One module defines a `MySQLDatabaseAccess` singleton\, which is found correctly\. So far\, everything looks fine\.

But then\, another module defines `MongoDBDatabaseAccess` \, because it stores reporting data in a Mongo DB\. All of a sudden\, those two modules cannot be used together\, because they introduce two `DatabaseAccess` singletons to the classpath\.

Even though this case could in theory be resolved\, by adding your own discriminator methods for resolving scope\, there are more arguments why Objectscan is not suited for DI\:

* It is not very good at error handling\. If your database connection cannot be established\, the error will be hard to trace\.

* It does not handle the case of circular dependencies\.

* It does not offer any configuration options\, for example\, for injecting your database password from a secure source\.

## Usage
Objectscan relies on the assumption that all scans yield the same result during the JVM lifetime\, no matter at what time they are called\. This also implies that all scan results can be cached\, as future calls with the same parameters would return the same results anyway\.

The stability assumption can in theory be violated \(by implementing `ObjectScanSource` in an unstable way\)\, which is strongly advised against\. This would lead to inconsistent behavior of the application\.

### Basic usage
#### Creating an instance of All
The main entry point for all code using Objectscan is `net.jackadull.objectscan.All` \. Creating an instance of `All` does not yet initiate any scanning operation\. It is just a preparation for upcoming scans\.

Probably\, you may want to reuse your `All` instance\, because it may cache its results\.

At first\, an instance of `All` must be created with some limiting factor\, or otherwise classpath scans of bigger projects can become very inefficient\. Usually\, you can either limit the package prefix of the scanned objects\, or determine that they must be nested inside another singleton\.

```scala
import net.jackadull.objectscan.All
val all = All.withinPackagePrefixes(Seq("net.jackadull.example", "com.mycompany"))
```
The above example will scan all class files whose package begins with either `net.jackadull.exampe` or `com.mycompany` \.

```scala
object MyEnclosingSingleton 
val all = All.nestedInside(MyEnclosingSingleton)
```
This will only scan all class files that are nested inside `MyEnclosingSingleton` \.

Note\: If you really want to scan _the whole_ classpath\, use `All.withinPackagePrefixes(Seq())` \. Use at your own risk though\, as these scans grow proportionally with classpath size\.

#### Running scans
Here are some examples for classpath scans\:

```scala
val all = ??? // see above
all.of[SomeType].toSeq               // returns Seq[SomeType]
all.of[SomeType].where(_.isActive)   // returns a filtered Seq[SomeType]
all.of[SomeType].isEmpty             // true if there are no instances of SomeType
all.of[SomeType].nonEmpty            // true if there is at least one singleton instance of SomeType
```
The examples above give a good overview over the possible operations on `All` instances\.

