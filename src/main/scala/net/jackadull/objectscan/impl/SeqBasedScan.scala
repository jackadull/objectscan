package net.jackadull.objectscan.impl

import net.jackadull.objectscan.Scan

private[impl] final class SeqBasedScan[A](seq:Seq[A]) extends Scan[A] {
  override def where(p:A=>Boolean):Seq[A] = seq.filter(p)
  override def isEmpty:Boolean = seq.isEmpty
  override def toSeq:Seq[A] = seq

  override def toString:String = if(isEmpty) "Scan()" else s"Scan(${seq.head}, ...)"
}
