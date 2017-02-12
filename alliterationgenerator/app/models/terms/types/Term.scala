package models.terms.types

import util.{CharacterGenerator, TermDatabase}

/**
 * Represents a term
 *
 * @constructor Accepts a character.
 */
class Term(val name: String, val isCaseSensitive: Boolean = false) extends Token {
  def sourceName(letter: Char) = name + "_" + letter
}
