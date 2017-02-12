package models.terms

import scala.util.Random

class GrammarState {

  val tense = newTense // past, present, perfect
  val isConditional = false // bool (allows conditional phrases)
  val nextLetter = newLetter

  private def newTense = Tense(Random.nextInt(Tense.maxId))
  private def newLetter = 'a' + Random.nextInt(26)
}

object GrammarState {
  def apply = new GrammarState

  def apply(state: GrammarState) =
}

object Tense extends Enumeration {
  val Past, Present, Future = Value
}
