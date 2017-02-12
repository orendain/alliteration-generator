package util

import play.api._
import play.api.mvc._
import play.Play.application

import java.nio.file._
import java.nio.charset.StandardCharsets

// TODO: Record submission in database

object Submitter {

  /**
   *
   */
  def submitAddText(term: String, text: String)(implicit request: RequestHeader) {
    val filePath = generateFilePath(term)
    saveTextToFile(text, filePath)
    sendSubmitMail(term, filePath)
  }

  /**
   *
   */
  def submitAddFile(term: String, file: java.io.File, fileName: String)(implicit request: RequestHeader) {
    val filePath = generateFilePath(term)
    prepareFilePath(filePath)
    Files.move(file.toPath, filePath)

    Logger.info("[SubmissionAgent:submitFile] Submitted file saved: " + filePath)

    sendSubmitMail(term, filePath, Some(fileName))
  }

  /**
   *
   */
  def submitContact(email: String, text: String)(implicit request: RequestHeader) {
    val mail = Mailer.Mail (
      to = Seq(play.Play.application.configuration.getString("mail.smtp.defaultNotifyEmail")),
      subject = "New Contact Submission",
      message =
        "From: " + email + " (" + request.remoteAddress + ")\n" +
        "On: " + new java.sql.Timestamp(new java.util.Date().getTime) + "\n\n" +
        "Message:\n" + text
    )

    Mailer.send(mail)
  }

  /**
   * @TODO: Test and handle exceptions
   */
  private def saveTextToFile(text: String, filePath: Path) {
    prepareFilePath(filePath)
    val writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)
    writer.write(text, 0, text.length)
    writer.close()
    Logger.info("[SubmissionAgent:saveTextToFile] Text saved to file: " + filePath)
  }

  /**
   *
   */
  private def prepareFilePath(filePath: Path) {
    if (!Files.exists(filePath.getParent)) {
      Files.createDirectories(filePath.getParent)
    }
  }

  /**
   *
   */
  private def generateFilePath(term: String) = {
    val timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new java.util.Date)
    Paths.get(application.path.toString)
      .resolve(application.configuration.getString("files.submissions.basePath"))
      .resolve(term + "_" + timestamp + ".txt")
  }

  /**
   *
   */
  private def sendSubmitMail(term: String, filePath: Path, originalFileName: Option[String] = None)(implicit request: RequestHeader) {
    val relativePath = Paths.get(application.path.toString).relativize(filePath)
    val fileUrl = controllers.routes.Forms.viewGenerate(None).absoluteURL() + relativePath

    val mail = Mailer.Mail (
      to = Seq(application.configuration.getString("mail.smtp.defaultNotifyEmail")),
      subject = "New Upload Submission",
      message =
        "From: (" + request.remoteAddress + ")\n" +
        "On: " + new java.sql.Timestamp(new java.util.Date().getTime) + "\n\n" +
        "Submit method: " +
          (originalFileName match {
            case Some(s) => "File (" + s + ")"
            case None => "Text"
          }) + "\n" +
        "Term type: " + term.capitalize + "\n" +
        "File URL: " + fileUrl
    )

    Mailer.send(mail)
  }
}
