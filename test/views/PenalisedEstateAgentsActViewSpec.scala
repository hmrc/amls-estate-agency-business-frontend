package views

import controllers.routes
import forms.PenalisedEstateAgentsActFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.PenalisedEstateAgentsActView

class PenalisedEstateAgentsActViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "penalisedEstateAgentsAct"

  val form = new PenalisedEstateAgentsActFormProvider()()

  "PenalisedEstateAgentsAct view" must {

    val view = viewFor[PenalisedEstateAgentsActView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, routes.PenalisedEstateAgentsActController.onSubmit(NormalMode).url)
  }
}
