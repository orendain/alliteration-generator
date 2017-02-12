package util

import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.ProvenShape
//import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.Config.driver.simple._

import java.sql.Timestamp

import play.api.Logger
import play.api.Play.current

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * The object responsible for managing the database where generated sentences are stored.
 */
object SentenceDatabase {

  private val sentences = TableQuery[SentenceTable]

    /**
   * Adds a sentence to the sentence database.
   *
   * @oaram sentence The sentence to add to the database.
   */
  def add(sentence: String) = Future {
    checkAndCreate()
    play.api.db.slick.DB.withSession { implicit session =>
      val currentTime = new Timestamp(new java.util.Date().getTime)
      sentences += (0, sentence, currentTime)
      //Logger.debug("[SentenceDatabase:add] Sentence added to database: " + sentence)
    }
  }

  def readAll() {
    play.api.db.slick.DB.withSession { implicit session =>
      sentences.list.foreach {x => Logger.debug(x.toString)}
    }
  }

  def drop() {
    play.api.db.slick.DB.withSession { implicit session =>
      sentences.ddl.drop
    }
  }

  /** Create any appropriate database tables, if necessary. */
  private def checkAndCreate() {
    play.api.db.slick.DB.withSession { implicit session =>
      if (MTable.getTables(sentences.baseTableRow.tableName).list.isEmpty) {
      //if (MTable.getTables("SENTENCES").list(session).isEmpty) {
        sentences.ddl.create
        Logger.debug("[SentenceDatabase:checkAndCreate] Sentence table created")
      }
    }
  }

  /** Schema for the table holding sentences. */
  private class SentenceTable(tag: Tag) extends Table[(Int, String, Timestamp)](tag, "SENTENCES") {
    def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def sentence: Column[String] = column[String]("SENTENCE", O.NotNull)
    def timestamp: Column[Timestamp] = column[Timestamp]("TIMESTAMP", O.NotNull)

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[(Int, String, Timestamp)] = (id, sentence, timestamp)
  }
}
