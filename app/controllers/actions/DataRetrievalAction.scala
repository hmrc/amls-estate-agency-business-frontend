/*
 * Copyright 2024 HM Revenue & Customs
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

package controllers.actions

import javax.inject.Inject
import models.requests.{IdentifierRequest, OptionalDataRequest}
import play.api.mvc.ActionTransformer
import repositories.AMLSFrontEndSessionRepository
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class DataRetrievalActionImpl @Inject() (
  val sessionRepository: AMLSFrontEndSessionRepository
)(implicit val executionContext: ExecutionContext)
    extends DataRetrievalAction {

  override protected def transform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = {

    implicit val hc = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    sessionRepository.get(request.credId).map {
      case None              =>
        OptionalDataRequest(request.request, request.credId, request.amlsRefNumber, request.accountTypeId, None)
      case Some(userAnswers) =>
        OptionalDataRequest(
          request.request,
          request.credId,
          request.amlsRefNumber,
          request.accountTypeId,
          Some(userAnswers)
        )
    }
  }
}

trait DataRetrievalAction extends ActionTransformer[IdentifierRequest, OptionalDataRequest]
