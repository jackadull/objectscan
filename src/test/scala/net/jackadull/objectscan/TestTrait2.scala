package net.jackadull.objectscan

trait TestTrait2 {
  def index:Int
}
object TestTrait2 {
  object Impl1 extends TestTrait2 {def index=0}
  object Impl2 extends TestTrait2 {def index=1}
}
