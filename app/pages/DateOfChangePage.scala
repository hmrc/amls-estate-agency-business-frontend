package pages

import java.time.LocalDate

import play.api.libs.json.JsPath

case object DateOfChangePage extends QuestionPage[LocalDate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "dateOfChange"
}
