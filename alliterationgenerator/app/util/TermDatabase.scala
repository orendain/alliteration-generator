package util

import models.terms.types.Term

import java.io.File
import scala.io.Source

import com.redis._

import play.api.Logger

/**
 * The object responsible for managing the database where terms are stored.
 */
object TermDatabase {

  /**
   * @todo Move configuration to conf/application file
   */
  private val clients = new RedisClientPool(
    host = "pub-redis-12340.us-west-2-1.1.ec2.garantiadata.com",
    port = 12340, secret = Option("allitgen")
  )

  /**
   * Get a random term from the database.
   *
   * @param term [[models.terms.types.Term]] The type of term to get.
   * @tparam String testDoc stuff.
   *
   * @return A term, as a String.
   */
  def get(termType: Term, letter: Char) = {
    clients.withClient { client => {
      client.srandmember(termType.sourceName(letter)).getOrElse { "TermNotFound" }
    }}
  }

  /**
   *
   */
  def uploadFromFile(termType: Term, file: File) {
    val source = Source.fromFile(file)
    uploadFromIterable(termType, source.getLines().toIterable)
    source.close()
  }

  /**
   *
   */
  def uploadFromText(termType: Term, text: String) {
    uploadFromIterable(termType, text.lines.toIterable)
  }

  /**
   * Adds a term to the database.
   *
   * @todo Check if term already exists in table.
   *
   * @param term The term to store.
   * @param term The type of term this term is.
   */
  private def add(termType: Term, terms: Iterable[String]) { //terms.size match {
    terms foreach { term => add(termType, term) }
  }

  /**
   * Adds a term to the database.
   *
   * @todo Check if term already exists in table.
   *
   * @param term The term to store.
   * @param term The type of term this term is.
   */
  private def add(termType: Term, term: String) {
    clients.withClient { client => {
      if (client.sadd(termType.sourceName(term(0).toLower), term).get > 0)
        Logger.info("[TermDatabase:add] Term saved to the database: " + term)
      else
        Logger.info("[TermDatabase:add] Term already in database: " + term)
    }}
  }

  /* Helper functions */
  private def uploadFromIterable(termType: Term, terms: Iterable[String]) {
    add(termType, termType.isCaseSensitive match {
      case true => terms map (_.trim().toLowerCase)
      case false => terms map (_.trim())
    })
  }
}
