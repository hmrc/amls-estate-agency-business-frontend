package views

import controllers.routes
import forms.PenalisedEstateAgentsActDetailFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.PenalisedEstateAgentsActDetailView

class PenalisedEstateAgentsActDetailViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "penalisedEstateAgentsActDetail"

  val form = new PenalisedEstateAgentsActDetailFormProvider()()

  "PenalisedEstateAgentsActDetailView view" must {

    val view = viewFor[PenalisedEstateAgentsActDetailView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like stringPage(form, applyView, messageKeyPrefix, routes.PenalisedEstateAgentsActDetailController.onSubmit(NormalMode).url)
  }
}
