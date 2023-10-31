/*
 * Copyright 2023 HM Revenue & Customs
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

import base.SpecBase
import models.UserAnswers
import models.requests.{IdentifierRequest, OptionalDataRequest}
import org.mockito.Mockito._
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.mockito.Matchers.{eq => eqTo}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import repositories.{AMLSFrontEndSessionRepository}
import uk.gov.hmrc.auth.core.AffinityGroup

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataRetrievalActionSpec extends SpecBase with MockitoSugar with ScalaFutures {

  class Harness(sessionRepository: AMLSFrontEndSessionRepository) extends DataRetrievalActionImpl(sessionRepository) {
    def callTransform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = transform(request)
  }

  "Data Retrieval Action" when {

    "there is no data in the cache" must {

      "set userAnswers to 'None' in the request" in {

        val sessionRepository = mock[AMLSFrontEndSessionRepository]
        when(sessionRepository.get(eqTo("CredId"))(any())) thenReturn Future(None)
        val action = new Harness(sessionRepository)

        val futureResult = action.callTransform(new IdentifierRequest(
          fakeRequest,
          Some("amlsRef"),
          "CredId",
          ("foo", "bar"),
          AffinityGroup.Organisation))

        whenReady(futureResult) { result =>
          result.userAnswers.isEmpty mustBe true
        }
      }
    }

    "there is data in the cache" must {

      "build a userAnswers object and add it to the request" in {

        val sessionRepository = mock[AMLSFrontEndSessionRepository]
        when(sessionRepository.get(eqTo("CredId"))(any())) thenReturn Future(Some(new UserAnswers()))
        val action = new Harness(sessionRepository)

        val futureResult = action.callTransform(new IdentifierRequest(
          fakeRequest,
          Some("amlsRef"),
          "CredId",
          ("foo", "bar"),
          AffinityGroup.Organisation))

        whenReady(futureResult) { result =>
          result.userAnswers.isDefined mustBe true
        }
      }
    }
  }
}
