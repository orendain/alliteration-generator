package models.terms.types

class Literal(val name: String) extends Token {

}

object Literal {
  implicit def stringToLiteral(str: String) = new Literal(str)

  def apply(name: String) = new Literal(name)
}
