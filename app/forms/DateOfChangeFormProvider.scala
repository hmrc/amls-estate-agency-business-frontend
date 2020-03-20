package forms

import java.time.LocalDate

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class DateOfChangeFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "dateOfChange.error.invalid",
        allRequiredKey = "dateOfChange.error.required.all",
        twoRequiredKey = "dateOfChange.error.required.two",
        requiredKey    = "dateOfChange.error.required"
      )
    )
}
