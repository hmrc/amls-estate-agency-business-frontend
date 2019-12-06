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

package views

import views.behaviours.ViewBehaviours
import views.html.WhatYouNeedView

class WhatYouNeedViewSpec extends ViewBehaviours {

  "WhatYouNeed view" must {

    val view = viewFor[WhatYouNeedView](Some(emptyUserAnswers))

    val applyView = view.apply()(fakeRequest, messages)

    behave like normalPage(applyView, "whatYouNeed")

    behave like pageWithBackLink(applyView)

    "include the correct content" in {
      val document = asDocument(applyView)

      assertTitleEqualsMessage(document, "title", "What you need")
      assertPageTitleEqualsMessage(document, "What you need")

      assertContainsText(document, "Automatic saving")
      assertContainsText(document, "Information is saved automatically. If you sign out, you’ll have 28 days to complete your application.")

      assertContainsText(document, "In this section you’ll be asked things like:")
      assertContainsText(document, "which estate agency services you provide")
      assertContainsText(document, "if you’re registered with a redress scheme")
      assertContainsText(document, "if your business has been penalised in the past")
    }
  }
}
