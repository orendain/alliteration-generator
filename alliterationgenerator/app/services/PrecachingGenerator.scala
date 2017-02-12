package services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import models.terms.Sentence

import scaldi._

class PrecachingGenerator(implicit inj: Injector) extends GeneratorService with Injectable {

  private[this] val tokenBuilder = inject [TokenBuilder]
  private[this] type SentenceQueue = java.util.concurrent.ConcurrentLinkedQueue[String]

  private val GrowthStepSize = 2
  private val queues: Vector[SentenceQueue] = Vector(buildQueues(): _*)

  /**
   *
   */
  def generateAlliterative(letter: Char) = Future {
    letterToIndex(letter) match {
      case Some(i) => getFrom(queues(i), letter)
      case None => throw new IllegalArgumentException("Invalid letter")
    }
  }

  /**
   *
   */
  def generateRegular() = Future { getFrom(queues(26), '?') }

  /**
   * If a queue is every completely empty, it permanently gets GrowthStepSize number
   * of additional sentences queued, in addition to what it previously had.
   */
  private def getFrom(queue: SentenceQueue, letter: Char) = {
    queue.isEmpty match {
      case false => {
        loadQueueDelayed(queue, letter, 1)
        queue.poll()
      }
      case true => {
        loadQueueDelayed(queue, letter, GrowthStepSize)
        loadQueue(queue, letter, 1)
        queue.poll()
      }
    }
  }

  /* Queue builder */
  private def buildQueues() = (('a' to 'z') :+ '?') map { letter =>
    val queue = new SentenceQueue()
    loadQueueDelayed(queue, letter, GrowthStepSize)
    queue
  }

  /**
   *
   */
  private def loadQueueDelayed(queue: SentenceQueue, letter: Char, amount: Int) = Future {
    loadQueue(queue, letter, amount)
  }

  /**
   *
   */
  private def loadQueue(queue: SentenceQueue, letter: Char, amount: Int) {
    if (amount > 0) {
      val sentence = tokenBuilder.build(Sentence, letter)
      queue.add(sentence)
      loadQueue(queue, letter, amount-1)
    }
  }

  /* Helper methods */
  /* TODO: Use regex since it may be faster */
  private def letterToIndex(letter: Char) = letter match {
    case x if ('a' to 'z') contains x => Some((x - 'a').toInt)
    case x if ('A' to 'Z') contains x => Some((x - 'A').toInt)
    case _ => None
  }
}
