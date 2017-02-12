package controllers

import play.api._
import play.api.mvc._

import models.StatusBar

object Pages extends Controller {

  //implicit val NoStatusBar: Option[StatusBar] = None
  def viewAbout(statusBar: Option[StatusBar]) = Action {
    Ok(views.html.about(statusBar))
  }

  def viewContribute(statusBar: Option[StatusBar]) = Action {
    Ok(views.html.contribute(statusBar))
  }
}
