package net.jackadull.objectscan

trait ObjectscanInfo {
  def artifactID:String = "${project.artifactId}"
  def groupID:String = "${project.groupId}"
  def version:String = "${project.version}"
}
object ObjectscanInfo extends ObjectscanInfo
