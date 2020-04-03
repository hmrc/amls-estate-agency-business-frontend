/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import controllers.actions._
import forms.EabServicesProvidedFormProvider
import javax.inject.Inject
import models.{Mode, UserAnswers}
import navigation.Navigator
import pages.EabServicesProvidedPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.AMLSFrontEndSessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import utils.ControllerHelper
import views.html.EabServicesProvidedView

import scala.concurrent.{ExecutionContext, Future}

class EabServicesProvidedController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        sessionRepository: AMLSFrontEndSessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: EabServicesProvidedFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: EabServicesProvidedView,
                                        val controllerHelper: ControllerHelper
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>

      val preparedForm = request.userAnswers.getOrElse(UserAnswers()).get(EabServicesProvidedPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode))),

        value =>
          for {
            updatedAnswers       <- Future.fromTry(request.userAnswers.getOrElse(UserAnswers()).set(EabServicesProvidedPage, value))
            status               <- controllerHelper.getApplicationStatus(request.amlsRefNumber, request.accountTypeId)
            dateOfChangeRequired <- controllerHelper.requireDateOfChange(request.credId, updatedAnswers, status)
            _                    <- sessionRepository.set(request.credId, updatedAnswers)
          } yield Redirect(
            navigator.nextPage(EabServicesProvidedPage, mode, updatedAnswers, Some(dateOfChangeRequired))
          )
      )
  }
}
