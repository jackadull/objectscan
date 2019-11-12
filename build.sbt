import net.jackadull.build.dependencies.JackadullDependencies._

lazy val jackadull = net.jackadull.build.JackadullBuild.onTravis(name = "objectscan", version = "0.4.1-SNAPSHOT",
  basePackage = "net.jackadull.objectscan", capitalizedIdentifier = "Objectscan")

lazy val objectscanBuild:Project =
  project.in(file(".")).configure(jackadull.project.withDocs(docs), jackadull.dependencies(ScalaTest % Test, ScalaXML))
    .settings(libraryDependencies += "io.github.classgraph" % "classgraph" % "4.8.53")

lazy val docs = project.in(file("docs")).configure(jackadull.docs)
