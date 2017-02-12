package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.filters.csrf.CSRF

import models._
import util.Submitter

object Forms extends Controller {

  val generateForm = Form(
    mapping(
      "sentence_type" -> nonEmptyText,
      "letter" -> nonEmptyText(maxLength = 1),
      "sentences" -> text
    )(Generate.apply)(Generate.unapply)
  )

  val addForm = Form {
    mapping(
      "term" -> nonEmptyText,
      "text" -> text(maxLength = 4096)
    )(Add.apply)(Add.unapply)
  }

  val contactForm = Form (
    mapping(
      "email" -> email,
      "body" -> nonEmptyText(maxLength = 1024)
    )(Contact.apply)(Contact.unapply)
  )

  /**
   *
   */
  def viewGenerate(statusBar: Option[StatusBar]) = Action { implicit request =>
    Ok(views.html.generate(generateForm)(statusBar, CSRF.getToken(request).get, request2lang(request)))
  }

  /**
   *
   */
  def viewAdd(statusBar: Option[StatusBar]) = Action { implicit request =>
    Ok(views.html.add(addForm, TermTypes.pairs)(statusBar, CSRF.getToken(request).get))
  }

  /**
   *
   */
  def viewContact(statusBar: Option[StatusBar]) = Action { implicit request =>
    Ok(views.html.contact(contactForm)(statusBar, CSRF.getToken(request).get))
  }

  /**
   *
   */
  def submitGenerate = Action { implicit request =>
    generateForm.bindFromRequest.fold (
      formWithErrors => BadRequest(views.html.generate(formWithErrors)(Some(StatusBar.FormError), CSRF.getToken(request).get, request2lang(request))),
      generateForm => {
        Redirect(routes.Forms.viewGenerate(Some(StatusBar.AddSubmitted)))
      }
    )
  }

  /**
   *
   */
  def submitAdd = Action(parse.multipartFormData) { implicit request =>
    addForm.bindFromRequest.fold (
      formWithErrors => BadRequest(views.html.add(formWithErrors, TermTypes.pairs)(Some(StatusBar.FormError), CSRF.getToken(request).get)),
      addForm => {
        // User entered words into the text field
        if (!addForm.text.isEmpty) {
          Submitter.submitAddText(addForm.term, addForm.text)
          Redirect(routes.Forms.viewAdd(Some(StatusBar.AddSubmitted)))
        } // Else upload the attached file
        else {
          request.body.file("file").map { file =>
            Logger.debug("[Submit:submitWords] Uploaded file content type: " + file.contentType)
            isValidContentType(file.contentType) match {
              case true => {
                Submitter.submitAddFile(addForm.term, file.ref.file, file.filename)
                Redirect(routes.Forms.viewAdd(Some(StatusBar.AddSubmitted)))
              }
              case false => Redirect(routes.Forms.viewAdd(Some(StatusBar.FileTypeError)))
            }
          }.getOrElse { // No file found
            Redirect(routes.Forms.viewAdd(Some(StatusBar.FormError)))
          }
        }
      }
    )
  }

  /**
   *
   */
  def submitContact = Action { implicit request =>
    contactForm.bindFromRequest.fold (
      formWithErrors => BadRequest(views.html.contact(formWithErrors)(Some(StatusBar.FormError), CSRF.getToken(request).get)),
      contactForm => {
        Submitter.submitContact(contactForm.email, contactForm.body)
        Redirect(routes.Forms.viewContact(Some(StatusBar.ContactSubmitted)))
      }
    )
  }

  private def isValidContentType(contentType: Option[String]) = {
    if (contentType.isEmpty || ("""text/*""".r findFirstIn contentType.get).isEmpty)
      false
    else
      true
  }
}
