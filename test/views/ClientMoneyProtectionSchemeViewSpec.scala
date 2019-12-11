package views

import controllers.routes
import forms.ClientMoneyProtectionSchemeFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.ClientMoneyProtectionSchemeView

class ClientMoneyProtectionSchemeViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "clientMoneyProtectionScheme"

  val form = new ClientMoneyProtectionSchemeFormProvider()()

  "ClientMoneyProtectionScheme view" must {

    val view = viewFor[ClientMoneyProtectionSchemeView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, routes.ClientMoneyProtectionSchemeController.onSubmit(NormalMode).url)
  }
}
