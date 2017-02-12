package models.terms

import types._

import types.Literal.stringToLiteral

object Quantifier extends Phrase {
  val rules = List(
    List(Literal(" quantifier"))
  )
}

object DemonstrativeDetreminer extends Phrase {
  val rules = List(
    List(Literal(" demonstrativeDeterminer"))
  )
}

object PossessiveDeterminer extends Phrase {
  val rules = List(
    List(Literal(" possessiveDeterminer"))
  )
}

object Article extends Phrase {
  val rules = List(
    List(Literal(" the")), // DefiniteArticle
    List(Literal(" a(n)")) // IndefiniteArticle
    //List(Literal(" no")) // NegativeArticle
  )
}

object MassNounPhrase extends Phrase {
  val rules = List(
    List(MassNoun),
    List(Literal(" some"), MassNoun),
    List(Literal(" some of this"), MassNoun)
  )
}

object EndingPunctuation extends Phrase {
  val rules = List(
    List(Literal(".")),
    List(Literal("!"))
  )
}
