package controllers

//import play.api._
import play.api.mvc._

import util.Aggregator

object Admins extends Controller {

  def viewAggregate = Action {
    Aggregator.aggregate()
    Ok("Aggregated!")
  }
}
