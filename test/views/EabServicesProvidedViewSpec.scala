package views

import forms.EabServicesProvidedFormProvider
import models.{EabServicesProvided, NormalMode}
import play.api.Application
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.EabServicesProvidedView

class EabServicesProvidedViewSpec extends CheckboxViewBehaviours[EabServicesProvided] {

  val messageKeyPrefix = "eabServicesProvided"

  val form = new EabServicesProvidedFormProvider()()

  "EabServicesProvidedView" must {

    val view = viewFor[EabServicesProvidedView](Some(emptyUserAnswers))

    def applyView(form: Form[Set[EabServicesProvided]]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like checkboxPage(form, applyView, messageKeyPrefix, EabServicesProvided.options)
  }
}
