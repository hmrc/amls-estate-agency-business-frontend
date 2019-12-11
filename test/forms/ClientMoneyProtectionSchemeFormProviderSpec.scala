package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class ClientMoneyProtectionSchemeFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "clientMoneyProtectionScheme.error.required"
  val invalidKey = "error.boolean"

  val form = new ClientMoneyProtectionSchemeFormProvider()()

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
