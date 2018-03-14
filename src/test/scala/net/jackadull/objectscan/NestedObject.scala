package net.jackadull.objectscan

object NestedObject {
  object Impl2 extends TestTrait3
  object Impl3 extends TestTrait3

  object NestedAgain {
    object Impl4 extends TestTrait3
  }
}
