package models

import terms._

object TermTypes {

  val terms = Seq (
    Noun,
    Verb,
    Adverb
  )

  val pairs = terms.map { term =>
    (term.name, term.name.capitalize)
  }
}
