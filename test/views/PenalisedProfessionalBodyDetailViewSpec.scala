package views

import controllers.routes
import forms.PenalisedProfessionalBodyDetailFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.PenalisedProfessionalBodyDetailView

class PenalisedProfessionalBodyDetailViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "penalisedProfessionalBodyDetail"

  val form = new PenalisedProfessionalBodyDetailFormProvider()()

  "PenalisedProfessionalBodyDetailView view" must {

    val view = viewFor[PenalisedProfessionalBodyDetailView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like stringPage(form, applyView, messageKeyPrefix, routes.PenalisedProfessionalBodyDetailController.onSubmit(NormalMode).url)
  }
}
