import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
object GeneratorsSpec extends PlaySpecification {

  "Generators.getAlliterative" should {
    "be valid for all lower/upper case characters" in new WithApplication {
      val controller = Global.getControllerInstance(classOf[controllers.Generators])
      (('a' to 'z') ++ ('A' to 'Z')) map { value =>
        val result: Future[play.api.mvc.Result] = controller.getAlliterative(value.toString).apply(FakeRequest())
        status(result) must be equalTo(OK)
        contentAsString(result).length must be greaterThan 0
      }
    }

    val badChars = """(0123456789~`!@#$%^&*-_=+{[}]|;< ,>.?'/\:)"""
    ("yield a BAD_REQUEST status for any character part of " + badChars) in new WithApplication {
      val controller = Global.getControllerInstance(classOf[controllers.Generators])
      (badChars.toList) map { value =>
        val result: Future[play.api.mvc.Result] = controller.getAlliterative(value.toString).apply(FakeRequest())
        status(result) must be equalTo(BAD_REQUEST)
      }
    }
  }

  "Generators.getRegular" should {
    "yield an OK result with a body of at least one character" in new WithApplication {
      val controller = Global.getControllerInstance(classOf[controllers.Generators])
      val result: Future[play.api.mvc.Result] = controller.getRegular().apply(FakeRequest())
      status(result) must be equalTo(OK)
      contentAsString(result).length must be greaterThan 0
    }
  }
}
