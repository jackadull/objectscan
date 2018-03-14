package net.jackadull.objectscan.impl

import net.jackadull.objectscan.{All, ObjectScanSource, Scan}

import scala.language.postfixOps
import scala.reflect.ClassTag

private[objectscan] class SourceScanningImpl(allOnClasspath:All) extends All {
  override def of[A](implicit t:ClassTag[A]):Scan[A] = {
    val onCP:Scan[A] = allOnClasspath.of[A]
    objectsFromSources get t.runtimeClass match {
      case None ⇒ onCP
      case Some(untypedObjs) ⇒
        val objs = untypedObjs.asInstanceOf[Set[A]]
        new SeqBasedScan(onCP.toSeq.filterNot(objs) ++ objs.toSeq)
    }
  }

  private lazy val objectsFromSources:Map[Class[_],Set[AnyRef]] = {
    val allSources = allOnClasspath.of[ObjectScanSource].toSeq
    allSources.foldLeft[Map[Class[_],Set[AnyRef]]](Map()) {(acc,source)⇒
      source.scannableObjects.foldLeft(acc) {(acc2,obj)⇒
        if(obj==null) acc2
        else allTypesInHierarchyOf(obj getClass).toSeq.foldLeft(acc2) {(acc3,cls)⇒
          acc3 get cls match {
            case Some(objs) ⇒ acc3 + (cls → (objs+obj))
            case None ⇒ acc3 + (cls → Set(obj))
          }
        }
      }
    }
  }

  private def allTypesInHierarchyOf(t:Class[_]):Set[Class[_]] = allParentTypes(t)+t

  private def allParentTypes(t:Class[_], seen:Set[Class[_]]=Set()):Set[Class[_]] =
    if(t==null || seen(t)) Set()
    else if(t==classOf[Object]) Set(t)
    else {
      val immediateSupers:Set[Class[_]] =
        (t.getInterfaces.toSet ++ Option(t.getSuperclass).toSet) -- seen
      val seen2 = seen ++ immediateSupers
      immediateSupers ++ (immediateSupers flatMap {s ⇒ allParentTypes(s, seen2)})
    }

  // TODO toString
}
