package views

import controllers.routes
import forms.PenalisedProfessionalBodyFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.PenalisedProfessionalBodyView

class PenalisedProfessionalBodyViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "penalisedProfessionalBody"

  val form = new PenalisedProfessionalBodyFormProvider()()

  "PenalisedProfessionalBody view" must {

    val view = viewFor[PenalisedProfessionalBodyView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, routes.PenalisedProfessionalBodyController.onSubmit(NormalMode).url)
  }
}
