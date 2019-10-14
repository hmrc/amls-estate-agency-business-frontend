package forms

import forms.behaviours.CheckboxFieldBehaviours
import models.EabServicesProvided
import play.api.data.FormError

class EabServicesProvidedFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new EabServicesProvidedFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "eabServicesProvided.error.required"

    behave like checkboxField[EabServicesProvided](
      form,
      fieldName,
      validValues  = EabServicesProvided.values,
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
