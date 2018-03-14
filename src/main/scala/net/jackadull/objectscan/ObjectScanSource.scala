package net.jackadull.objectscan

trait ObjectScanSource {
  def scannableObjects:Iterable[AnyRef]
}
