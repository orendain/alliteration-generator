package util

import play.api._
import play.api.mvc._

import play.api.libs.oauth._
import play.api.libs.ws.WS

import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

object Twitter {

  /** Twitter OAuth consumer key */
  val AllitConsumerKey = ConsumerKey(
    "0vsvhIa0ZeT8weGGwOJkt2aMl",
    "ImO3FFx03kjH9M6P1Ai4qQqKDQgZ4SXHUTMyaLpuGup3nGWlNs")

  /** Twitter client access token */
  val AccessToken = RequestToken(
    "2491055491-9z77ggij9cfG18mleCBUHaiWpNC39aUBLAxBtn2",
    "SOzBbkapoGJumNo1vBrZxdWgawYfgKQno00OGcLWcqwsC")

  /**
   * Send a tweet with the specified message.
   *
   * @todo Parse result to see if it failed
   * @param message The text to tweet.
   */
  def tweet(message: String) = {
    WS.url("https://api.twitter.com/1.1/statuses/update.json")
      .sign(OAuthCalculator(AllitConsumerKey, AccessToken))
      .withHeaders("Content-Type" -> "x-www-form-urlencoded")
      .withQueryString("status" -> formatMessage(message))
      .withQueryString("trim_user" -> "t")
      .post("content")
      .map { response => response.status match {
          case 200 => Logger.info("[Twitter:tweet] Tweet successfully made")
          case s => Logger.error("[Twitter:tweet] Failed with (status, statusTxt, json): (" +
            response.status + ", " + response.statusText + ", " + response.json + ")")
      }}
  }

  /**
   * Format a message (sentence) into a proper tweet.
   *
   * @param message The message to format.
   * @return A prepared message ready to be tweeted.
   */
  private def formatMessage(message: String) = {
    val postMsg = " AlliterationGenerator.com #alliteration"
    val maxLen = 140 - postMsg.length
    // Max t.co wrapper length: https://dev.twitter.com/docs/api/1.1/get/help/configuration
    if (message.length > maxLen) {
      message.substring(0, maxLen-3) + "..." + postMsg
    }
    else {
      message + postMsg
    }
  }

  /* Used only for initial token retrieval */

  /*
  val TwitterService = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", AllitConsumerKey),
    true)
  */

  /**
   * Authenticate a client and retrieve necessary tokens.
   *//*
  def authenticate = Action { request =>
    request.getQueryString("oauth_verifier").map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TwitterService.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Pages.about).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    }.getOrElse(
      TwitterService.retrieveRequestToken("http://localhost:9000/twitter/auth_callback") match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          Redirect(TwitterService.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      })
  }*/

  /*
  private def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }*/
}
