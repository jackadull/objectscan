package net.jackadull.objectscan.impl

import net.jackadull.objectscan.Scan

import scala.language.postfixOps

private[impl] final class SeqBasedScan[A](seq:Seq[A]) extends Scan[A] {
  def where(p:A⇒Boolean) = seq filter p
  def isEmpty = seq isEmpty
  def toSeq = seq

  override def toString = if(isEmpty) "Scan()" else s"Scan(${seq head}, ...)"
}
