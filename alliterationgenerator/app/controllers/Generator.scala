package controllers

import play.api._
import play.api.mvc._

import play.api.Logger

import services.GeneratorService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import util.{CharacterGenerator, SentenceDatabase, Twitter}

import scaldi._

class Generator(implicit inj: Injector) extends Controller with Injectable {

  val generatorService = inject [GeneratorService]

  /**
   *
   */
  def getAlliterative(letter: String) = Action.async { request =>

    if (!("""\?""".r findFirstIn  letter).isEmpty) {
      generatorService.generateAlliterative(CharacterGenerator.getNext()).map { sentence =>
        recordSentence(sentence)
        Ok(sentence)
      }
    }
    else if (!("""[a-zA-Z]""".r findFirstIn  letter).isEmpty) {
      generatorService.generateAlliterative(letter(0)).map { sentence =>
        recordSentence(sentence)
        Ok(sentence)
      }
    }
    else {
      Future { BadRequest("Invalid character argument.") }
    }
  }

  /**
   *
   */
  def getRegular() = Action.async { request =>
    generatorService.generateRegular().map { sentence =>
      recordSentence(sentence)
      Ok(sentence)
    }
  }

  /**
   *
   */
  private def recordSentence(sentence: String) {
    Future {
      //SentenceDatabase.add(sentence)
      //Twitter.tweet(sentence)
    }
  }
}
