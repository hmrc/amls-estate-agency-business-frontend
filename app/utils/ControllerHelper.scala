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

package utils

import connectors.{AMLSBackEndConnector, AMLSConnector}
import javax.inject.Inject
import models.UserAnswers
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class ControllerHelper @Inject() (amlsConnector: AMLSConnector, amlsBackEndConnector: AMLSBackEndConnector) {

  def getApplicationStatus(amlsRefNo: Option[String], accountTypeId: (String, String))(implicit
    ec: ExecutionContext,
    hc: HeaderCarrier
  ): Future[String] =
    amlsRefNo match {
      case Some(number) => amlsBackEndConnector.status(number, accountTypeId).map(response => response.formBundleStatus)
      case None         => Future.successful("NotYetSubmitted")
    }

  def requireDateOfChange(credId: String, userAnswers: UserAnswers, status: String)(implicit
    ec: ExecutionContext,
    hc: HeaderCarrier
  ) =
    amlsConnector.requireDateOfChange(credId, status, userAnswers).map(r => r.requireDateOfChange)
}
