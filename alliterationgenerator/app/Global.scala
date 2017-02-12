import play.api._
import play.api.mvc._
import play.filters.csrf._

import scaldi.play.ScaldiSupport

import modules._

object Global extends WithFilters(CSRFFilter()) with GlobalSettings with ScaldiSupport {

  //def applicationModule = new AllModule :: new StandardModule
  def applicationModule = new AllModule :: new TestModule
}
