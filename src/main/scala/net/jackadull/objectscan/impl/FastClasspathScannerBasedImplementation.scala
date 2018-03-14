package net.jackadull.objectscan.impl

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import net.jackadull.objectscan.All

import scala.collection.JavaConverters.asScalaBuffer
import scala.reflect.ClassTag

private[objectscan] class FastClasspathScannerBasedImplementation(packagePrefixes:Seq[String]) extends All {
  // TODO a cache would greatly help here (or a smarter tree structure)
  def of[A](implicit tag:ClassTag[A]) = {
    val scanResult = new FastClasspathScanner(packagePrefixes:_*).scan
    val cls = tag.runtimeClass
    val candidateNames:Seq[String] = asScalaBuffer(
      scanResult.getNamesOfAllClasses // TODO too many results
      //if(cls isInterface) scanResult.getNamesOfClassesImplementing(cls) else scanResult.getNamesOfSubclassesOf(cls)
    ).toList
    def loadClassOpt(className:String):Option[Class[_]] =
      try {Some(Class forName className)} catch {case _:ClassNotFoundException ⇒ None}
    def loadClass(className:String):Seq[Class[_]] = {
      loadClassOpt(className).toSeq ++ loadClassOpt(className+"$").toSeq
    }
    val candidates:Seq[Class[_]] = candidateNames flatMap loadClass filter useClass
    new SeqBasedScan(candidates flatMap {candidate ⇒
      try {
        candidate.getField("MODULE$").get(candidate) match {
          case r:A ⇒ Some(r)
          case _ ⇒ None
        }
      } catch {
        case _:NoSuchFieldException ⇒ None
      }
    })
  }

  protected def useClass(cls:Class[_]):Boolean = true
  // TODO toString
}
