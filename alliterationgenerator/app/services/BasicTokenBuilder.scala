package services

import scala.util.Random
import models.terms.types._
import util.{CharacterGenerator, TermDatabase}

import scaldi._

class BasicTokenBuilder(implicit inj: Injector) extends TokenBuilder with Injectable {

  def build(token: Token, letter: Char) = {
    //tense = newTense
    buildToken(token, letter)
  }

  private def buildToken(token: Token, letter: Char) = {

    token match {
      case t: Literal => t.name

      case t: LetterlessTerm => " " + TermDatabase.get(t, letter)

      /*case t: Verb => {
        val le
      }*/

      case t: Term => {
        val letterValue = letter match {
          case '?' => CharacterGenerator.getNext()
          case x => x
        }

        " " + TermDatabase.get(t, letterValue)
      }

      case t: Phrase => {
        val rand = Random.nextInt(t.rules.length)
        val rule = t.rules(rand)

        val results = rule map (build(_, letter))
        results mkString
      }
    }
  }
}
