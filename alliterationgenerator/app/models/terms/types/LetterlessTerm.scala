package models.terms.types

import util.TermDatabase

class LetterlessTerm(override val name: String, override val isCaseSensitive: Boolean = false) extends Term(name, isCaseSensitive)
