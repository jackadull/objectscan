package net.jackadull.objectscan

import org.scalatest.{FreeSpec, Matchers}

class AllTest extends FreeSpec with Matchers {
  val all = All withinPackagePrefixes Seq("net.jackadull.objectscan")

  "All finds the only implemenation of a trait" in {
    all.of[TestTrait1].toSeq should be (Seq(TestTrait1))
  }
  "All finds both implementations of a trait" in {
    all.of[TestTrait2].toSeq.toSet should be (Set(TestTrait2.Impl1, TestTrait2.Impl2))
  }
  "All finds objects nested inside other objects" in {
    All.nestedInside(NestedObject).of[TestTrait3].toSeq.toSet should be(
      Set(NestedObject.Impl2, NestedObject.Impl3, NestedObject.NestedAgain.Impl4)
    )
  }
}
