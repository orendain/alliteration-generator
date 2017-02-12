package services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import util._

import scaldi._

trait GeneratorService {
  def generateAlliterative(letter: Char): Future[String]
  def generateRegular(): Future[String]
}
