#!/bin/bash

echo ""
echo "Applying migration RedressScheme"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /redressScheme                        controllers.RedressSchemeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /redressScheme                        controllers.RedressSchemeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRedressScheme                  controllers.RedressSchemeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRedressScheme                  controllers.RedressSchemeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "redressScheme.title = Which redress scheme is your business registered with?" >> ../conf/messages.en
echo "redressScheme.heading = Which redress scheme is your business registered with?" >> ../conf/messages.en
echo "redressScheme.notRegistered = Business is not registered with a redress scheme" >> ../conf/messages.en
echo "redressScheme.ombudsmanServices = Ombudsman Services" >> ../conf/messages.en
echo "redressScheme.checkYourAnswersLabel = Which redress scheme is your business registered with?" >> ../conf/messages.en
echo "redressScheme.error.required = Select redressScheme" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRedressSchemeUserAnswersEntry: Arbitrary[(RedressSchemePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RedressSchemePage.type]";\
    print "        value <- arbitrary[RedressScheme].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRedressSchemePage: Arbitrary[RedressSchemePage.type] =";\
    print "    Arbitrary(RedressSchemePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRedressScheme: Arbitrary[RedressScheme] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(RedressScheme.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RedressSchemePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def redressScheme: Option[AnswerRow] = userAnswers.get(RedressSchemePage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"redressScheme.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(messages(s\"redressScheme.$x\")),";\
     print "        routes.RedressSchemeController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration RedressScheme completed"
