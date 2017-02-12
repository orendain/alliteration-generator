package models.terms.types

trait Phrase extends Token {
  val rules: List[List[Token]]
}
