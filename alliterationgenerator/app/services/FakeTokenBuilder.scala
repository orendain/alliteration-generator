package services

import scala.util.Random
import models.terms.types._

object FakeTokenBuilder extends TokenBuilder {

  def build(token: Token, letter: Char) = {
    token match {
      case t: Literal => t.name
      case t: LetterlessTerm => " " + t.name
      case t: Term => " " + t.name + "_" + letter
      case t: Phrase => {
        val rand = Random.nextInt(t.rules.length)
        val rule = t.rules(rand)

        val results = rule map (build(_, letter))
        results mkString
      }
    }
  }
}
