package views

import forms.RedressSchemeFormProvider
import models.{NormalMode, RedressScheme}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.ViewBehaviours
import views.html.RedressSchemeView

class RedressSchemeViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "redressScheme"

  val form = new RedressSchemeFormProvider()()

  val view = viewFor[RedressSchemeView](Some(emptyUserAnswers))

  def applyView(form: Form[_]): HtmlFormat.Appendable =
    view.apply(form, NormalMode)(fakeRequest, messages)

  "RedressSchemeView" must {

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))
  }

  "RedressSchemeView" when {

    "rendered" must {

      "contain radio buttons for the value" in {

        val doc = asDocument(applyView(form))

        for (option <- RedressScheme.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for (option <- RedressScheme.options) {

      s"rendered with a value of '${option.value}'" must {

        s"have the '${option.value}' radio button selected" in {

          val doc = asDocument(applyView(form.bind(Map("value" -> s"${option.value}"))))

          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for (unselectedOption <- RedressScheme.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
