package util

import org.apache.commons.mail._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.Logger

// See: http://commons.apache.org/proper/commons-email
// and https://gist.github.com/mariussoutier/3436111
object Mailer {

  /**
   *  Sends an email
   */
  def send(mail: Mail) = Future {
    sendMail(mail)
  }

  /**
   *
   */
  private def sendMail(mail: Mail) {
    val preparedMail: Email = {
      if (mail.attachment.isDefined) {
        val attachment = new EmailAttachment()
        attachment.setPath(mail.attachment.get.getAbsolutePath)
        attachment.setDisposition(EmailAttachment.ATTACHMENT)
        attachment.setName(mail.attachment.get.getName)
        new MultiPartEmail().attach(attachment).setMsg(mail.message)
      }
      else if (mail.richMessage.isDefined) {
        new HtmlEmail().setHtmlMsg(mail.richMessage.get).setTextMsg(mail.message)
      }
      else {
        new SimpleEmail().setMsg(mail.message)
      }
    }

    // Cannot add these via fluent API because it produces exceptions
    mail.to foreach (preparedMail.addTo(_))
    mail.cc foreach (preparedMail.addCc(_))
    mail.bcc foreach (preparedMail.addBcc(_))

    // Configure mail host
    preparedMail.setHostName(mailHost)
    preparedMail.setSmtpPort(mailPort)
    preparedMail.setAuthenticator(mailAuth)
    preparedMail.setSSLOnConnect(mailSSL)

    preparedMail.setFrom(mailFromEmail, mailFromName)
    preparedMail.setSubject(mail.subject)
    //preparedMail.setDebug(true);

    try {
      preparedMail.send()
      Logger.info("[Mailer:sendMail] An email has been sent.")
    } catch {
      case e: IllegalStateException => Logger.error("[Mailer:sendMail] MimeMessage was already built: " + e)
      case e: EmailException => Logger.error("[Mailer:sendMail] Sending failed: " + e)
    }
  }

  /**
   *
   */
  case class Mail (
    to: Seq[String],
    cc: Seq[String] = Seq.empty,
    bcc: Seq[String] = Seq.empty,
    subject: String,
    message: String,
    richMessage: Option[String] = None,
    attachment: Option[(java.io.File)] = None
  )

  /* Configuration via application.conf */
  private[this] val config = play.Play.application.configuration
  private[this] val mailHost = config.getString("mail.smtp.host")
  private[this] val mailPort = config.getInt("mail.smtp.port")
  private[this] val mailSSL = config.getBoolean("mail.smtp.ssl")
  private[this] val mailFromEmail = config.getString("mail.smtp.fromEmail")
  private[this] val mailFromName = config.getString("mail.smtp.fromName")
  private[this] val mailAuth = new DefaultAuthenticator(
    config.getString("mail.smtp.authUser"),
    config.getString("mail.smtp.authPass")
  )
}
