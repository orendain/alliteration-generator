package services

import models.terms.Sentence

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scaldi._

class BasicGenerator(implicit inj: Injector) extends GeneratorService with Injectable {

  private[this] val tokenBuilder = inject [TokenBuilder]

  /**
   *
   */
  def generateAlliterative(letter: Char) = Future { tokenBuilder.build(Sentence, letter) }

  /**
   *
   */
  def generateRegular() = Future { tokenBuilder.build(Sentence, '?') }
}
