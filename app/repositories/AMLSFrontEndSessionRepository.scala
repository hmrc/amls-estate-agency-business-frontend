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

package repositories

import connectors.AMLSConnector
import javax.inject.Inject
import models.UserAnswers
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class DefaultAMLSFrontEndSessionRepository @Inject() (amlsConnector: AMLSConnector, config: Configuration)(implicit
  ec: ExecutionContext
) extends AMLSFrontEndSessionRepository {

  def get(credId: String)(implicit hc: HeaderCarrier): Future[Option[UserAnswers]] =
    amlsConnector.get(credId).map {
      _.map { json =>
        json.as[UserAnswers]
      }
    } recover { case _: Exception =>
      None
    }

  def set(credId: String, userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[Boolean] =
    amlsConnector.set(credId, userAnswers).map { result =>
      result.body.nonEmpty
    } recover { case _: Exception =>
      false
    }
}

trait AMLSFrontEndSessionRepository {
  def get(id: String)(implicit hc: HeaderCarrier): Future[Option[UserAnswers]]
  def set(credId: String, userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[Boolean]
}
