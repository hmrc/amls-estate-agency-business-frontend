/*
 * Copyright 2019 HM Revenue & Customs
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

import akka.stream.Materializer
import connectors.AMLSConnector
import javax.inject.Inject
import models.UserAnswers
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class DefaultAMLSFrontEndSessionRepository @Inject()(amlsConnector: AMLSConnector,
                                                     config: Configuration)
                                                    (implicit ec: ExecutionContext, m: Materializer) extends AMLSFrontEndSessionRepository {

  def get(id: String)(implicit hc: HeaderCarrier): Future[Option[UserAnswers]] =
    amlsConnector.get(id).map {
      _.map {
        json =>
          json.as[UserAnswers]
      }
    } recover {
      case _: Exception => throw new Exception("Failed")
    }

  def set(userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[Boolean] =
    amlsConnector.set(userAnswers.id, userAnswers).map { r =>
      r.isInstanceOf[UserAnswers]
    } recover {
      case e: Exception => throw new Exception(e.getMessage)
    }
}

trait AMLSFrontEndSessionRepository {

  def get(id: String)(implicit hc: HeaderCarrier): Future[Option[UserAnswers]]

  def set(userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[Boolean]
}
