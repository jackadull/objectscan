package net.jackadull.objectscan

import net.jackadull.objectscan.impl.{ClassGraphBasedImplementation, SourceScanningImpl}

import scala.annotation.tailrec
import scala.reflect.ClassTag

trait All {
  def of[A](implicit t:ClassTag[A]):Scan[A]
}
object All {
  def withinPackagePrefixes(packagePrefixes:Seq[String]):All =
    new SourceScanningImpl(new ClassGraphBasedImplementation(packagePrefixes))

  def nestedInside(obj:AnyRef):All =
    new SourceScanningImpl(new ClassGraphBasedImplementation(Seq(obj.getClass.getPackage.getName)) {
      private val outerClass:Class[_] = obj.getClass
      override protected def useClass(cls:Class[_]):Boolean = {
        @tailrec def recurse(c:Class[_]):Boolean =
          if(c==null) false else if(equivalent(c, outerClass)) true else recurse(c.getEnclosingClass)
        recurse(cls)
      }
      private def equivalent(cls1:Class[_], cls2:Class[_]):Boolean = {
        @tailrec def recurse(c1:String, c2:String):Boolean =
          if(c1 endsWith "$") recurse(c1 dropRight 1, c2)
          else if(c2 endsWith "$") recurse(c1, c2 dropRight 1)
          else c1==c2
        recurse(cls1.getName, cls2.getName)
      }
    })
}
