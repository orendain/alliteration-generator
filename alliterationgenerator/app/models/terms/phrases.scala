package models.terms

import types._

object Sentence extends Phrase {
  val rules = List(
    List(DeterminerPhrase, VerbPhrase, EndingPunctuation)
  )
}

/** http://en.wikipedia.org/wiki/Noun_phrase */
object NounPhrase extends Phrase {
  val rules = List(
    //List(AdjectivePhrase, NounPhrase), // Compile issue
    List(Noun)
  )
}

/** http://en.wikipedia.org/wiki/Determiner_phrase */
object DeterminerPhrase extends Phrase {
  val rules = List(
    List(Determiner, NounPhrase),
    List(Determiner, NounPhrase, AdverbPhrase),
    List(MassNounPhrase)
  )
}

object Determiner extends Phrase {
  val rules = List(
    List(Article),
    List(Quantifier), // (all, every, many, each, no?, etc.)
    //List(DemonstrativeDeterminer), // (this, that, those, etc.)
    List(PossessiveDeterminer) // (my, your, her, its their, etc.)
  )
}

/** http://en.wikipedia.org/wiki/Verb_phrase */
object VerbPhrase extends Phrase {
  val rules = List(
    List(Verb),
    List(Verb, AdverbPhrase),
    List(Verb, DeterminerPhrase),
    List(Verb, DeterminerPhrase, Adverb)
  )
}

/** http://en.wikipedia.org/wiki/Adjective_phrase **/
object AdjectivePhrase extends Phrase {
  val rules = List(
    List(Adverb, Adjective),
    List(Adverb, Adjective, AdverbPhrase),
    List(Adjective),
    List(Adjective, AdverbPhrase)
  )
}

object PrepositionalPhrase extends Phrase {
  val rules = List(
    List(Preposition, DeterminerPhrase)
  )
}

/** http://en.wikipedia.org/wiki/Adverbial_phrase */
/** http://en.wikipedia.org/wiki/Adverbial_clause */
object AdverbPhrase extends Phrase {
  val rules = List(
    List(Adverb),
    List(PrepositionalPhrase)
  )
}
