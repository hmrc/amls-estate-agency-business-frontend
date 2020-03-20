package views

import java.time.LocalDate

import forms.DateOfChangeFormProvider
import models.{NormalMode, UserAnswers}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.DateOfChangeView

class DateOfChangeViewSpec extends QuestionViewBehaviours[LocalDate] {

  val messageKeyPrefix = "dateOfChange"

  val form = new DateOfChangeFormProvider()()

  "DateOfChangeView view" must {

    val application = applicationBuilder(userAnswers = Some(UserAnswers(userAnswersId))).build()

    val view = application.injector.instanceOf[DateOfChangeView]

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))
  }
}
