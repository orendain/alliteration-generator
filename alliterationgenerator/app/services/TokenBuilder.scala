package services

import models.terms.types.Token

trait TokenBuilder {
  def build(token: Token, letter: Char): String
}
