package di.scratch.domain

object User {
  def apply(name: String) = new User(name)
}
class User(val name: String) {
  override def toString = name
}