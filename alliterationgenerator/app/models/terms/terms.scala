package models.terms

import types._

object Noun extends Term("noun")

object MassNoun extends Term("massnoun")

/** http://en.wikipedia.org/wiki/Verb and http://en.wikipedia.org/wiki/Auxiliary_verb */
object Verb extends Term("verb")

object Adjective extends Term("adjective")
object Adverb extends Term("adverb")
object Pronoun extends Term("pronoun")

/** http://en.wikipedia.org/wiki/Proper_noun */
object ProperNoun extends Term("propernoun", true)

object Preposition extends Term("preposition")
