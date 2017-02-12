package controllers

import play.api._
import play.api.mvc._

object Routing extends Controller {

  /**
   *
   */
  def get(path: String, file: String) = Action {
    import play.api.Play.current
    Ok.sendFile(Play.getFile(path + "/" + file))
  }

  /**
   *
   */
  def javascriptRoutes = Action { implicit request =>
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.Generator.getAlliterative,
        routes.javascript.Generator.getRegular
      )
    ).as("text/javascript")
  }
}
