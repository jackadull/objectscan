package net.jackadull.objectscan

trait Scan[+A] {
  def where(p:A⇒Boolean):Seq[A]

  def isEmpty:Boolean
  def toSeq:Seq[A]

  def nonEmpty:Boolean = !isEmpty
}
