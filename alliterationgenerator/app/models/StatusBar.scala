package models

import play.api.mvc.QueryStringBindable

//class StatusBar(val statusID: Int, override val text: String) extends play.twirl.api.Html(text)
class StatusBar(val statusID: Int, val html: play.twirl.api.Html)
//class StatusBar(val statusID: Int, val template: play.twirl.api.BaseScalaTemplate)
//class StatusBar(val statusID: Int, val template: play.twirl.api.BaseScalaTemplate[play.twirl.api.Html,play.twirl.api.Format[play.twirl.api.Html]])

object StatusBar {

  implicit def queryStringBinder(implicit intBinder: QueryStringBindable[Int]) = new QueryStringBindable[StatusBar] {

    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, StatusBar]] = {

      for {
        id <- intBinder.bind(key + ".id", params)
      } yield {
        id match {
          case Right(id) => id match {
            //case 0 => Right(Empty)
            case 1 => Right(FormError)
            case 2 => Right(ContactSubmitted)
            case 3 => Right(AddSubmitted)
            case 4 => Right(FileTypeError)
          }
          case _ => Left("Unable to bind a StatusBar")
        }
      }
    }

    override def unbind(key: String, StatusBar: StatusBar): String = {
      intBinder.unbind(key + ".id", StatusBar.statusID)
    }
  }

  /* The different status bars */
  //val Empty = new StatusBar(0, views.statusbars.html.empty)
  val FormError = new StatusBar(1, views.html.statusbars.formerror.render())
  val ContactSubmitted = new StatusBar(2, views.html.statusbars.contactsubmitted.render())
  val AddSubmitted = new StatusBar(3, views.html.statusbars.addsubmitted.render())
  val FileTypeError = new StatusBar(4, views.html.statusbars.filetypeerror.render())

  /*val listt = Seq(
    views.html.statusbars.formerror,
    views.html.statusbars.contactsubmitted
  )*/

  //logger.debug(listt)
}

//Seq[
/*
  play.twirl.api.BaseScalaTemplate[
    play.twirl.api.Html,
    play.twirl.api.Format[play.twirl.api.Html]
  ]
  with play.twirl.api.Template0[play.twirl.api.Html]]
*/
