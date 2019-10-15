#!/bin/bash

echo ""
echo "Applying migration RedressSchemeDetail"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /redressSchemeDetail                        controllers.RedressSchemeDetailController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /redressSchemeDetail                        controllers.RedressSchemeDetailController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRedressSchemeDetail                  controllers.RedressSchemeDetailController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRedressSchemeDetail                  controllers.RedressSchemeDetailController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "redressSchemeDetail.title = redressSchemeDetail" >> ../conf/messages.en
echo "redressSchemeDetail.heading = redressSchemeDetail" >> ../conf/messages.en
echo "redressSchemeDetail.checkYourAnswersLabel = redressSchemeDetail" >> ../conf/messages.en
echo "redressSchemeDetail.error.required = Enter redressSchemeDetail" >> ../conf/messages.en
echo "redressSchemeDetail.error.length = RedressSchemeDetail must be 256 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRedressSchemeDetailUserAnswersEntry: Arbitrary[(RedressSchemeDetailPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RedressSchemeDetailPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRedressSchemeDetailPage: Arbitrary[RedressSchemeDetailPage.type] =";\
    print "    Arbitrary(RedressSchemeDetailPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RedressSchemeDetailPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def redressSchemeDetail: Option[AnswerRow] = userAnswers.get(RedressSchemeDetailPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"redressSchemeDetail.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x),";\
     print "        routes.RedressSchemeDetailController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration RedressSchemeDetail completed"
