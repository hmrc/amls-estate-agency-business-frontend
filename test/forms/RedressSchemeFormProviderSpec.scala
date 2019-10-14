package forms

import forms.behaviours.OptionFieldBehaviours
import models.RedressScheme
import play.api.data.FormError

class RedressSchemeFormProviderSpec extends OptionFieldBehaviours {

  val form = new RedressSchemeFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "redressScheme.error.required"

    behave like optionsField[RedressScheme](
      form,
      fieldName,
      validValues  = RedressScheme.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
