package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class PenalisedEstateAgentsActFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "penalisedEstateAgentsAct.error.required"
  val invalidKey = "error.boolean"

  val form = new PenalisedEstateAgentsActFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
