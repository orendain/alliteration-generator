package util

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object CharacterGenerator {

  private val GrowthStepSize = 5
  private val queue = new java.util.concurrent.ConcurrentLinkedQueue[Char]

  /**
   *
   */
  def getNext() = queue.isEmpty() match {
    case false => {
      loadQueueDelayed(1)
      queue.poll()
    }
    case true => {
      loadQueue(1)
      loadQueueDelayed(GrowthStepSize)
      queue.poll()
    }
  }

  /**
   *
   */
  private def loadQueueDelayed(amount: Int) = Future {
    loadQueue(amount)
  }

  /**
   *
   */
  private def loadQueue(amount: Int) {
    if (amount > 0) {
      queue.add((scala.util.Random.nextInt(26) + 'a').toChar)
      loadQueue(amount-1)
    }
  }
}
