package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class PenalisedEstateAgentsActDetailFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "penalisedEstateAgentsActDetail.error.required"
  val lengthKey = "penalisedEstateAgentsActDetail.error.length"
  val maxLength = 255

  val form = new PenalisedEstateAgentsActDetailFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
