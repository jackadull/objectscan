package net.jackadull.objectscan.impl

import io.github.classgraph.{ClassGraph, ClassInfo}
import net.jackadull.objectscan.All

import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

private[objectscan] class ClassGraphBasedImplementation(packagePrefixes:Seq[String]) extends All {
  override def of[A](implicit tag:ClassTag[A]):SeqBasedScan[A] = {
    val scanResult = new ClassGraph().whitelistPackages(packagePrefixes:_*).scan
    try {
      val candidateInfos:Seq[ClassInfo] = scanResult.getAllClasses.asScala.toSeq
      def loadClassOpt(className:String):Option[Class[_]] =
        try {Some(Class.forName(className))} catch {case _:ClassNotFoundException => None}
      def loadClass(classInfo:ClassInfo):Seq[Class[_]] = loadClassOpt(classInfo.getName).toSeq
      val candidates:Seq[Class[_]] = candidateInfos.flatMap(loadClass).filter(useClass)
      new SeqBasedScan(candidates.flatMap {candidate =>
        try {
          candidate.getField("MODULE$").get(candidate) match {
            case r:A => Some(r)
            case _ => None
          }
        } catch {
          case _:NoSuchFieldException => None
        }
      })
    } finally {scanResult.close()}
  }

  protected def useClass(cls:Class[_]):Boolean = true
}
