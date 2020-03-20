#!/bin/bash

echo ""
echo "Applying migration DateOfChange"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /dateOfChange                  controllers.DateOfChangeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /dateOfChange                  controllers.DateOfChangeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDateOfChange                        controllers.DateOfChangeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDateOfChange                        controllers.DateOfChangeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "dateOfChange.title = DateOfChange" >> ../conf/messages.en
echo "dateOfChange.heading = DateOfChange" >> ../conf/messages.en
echo "dateOfChange.checkYourAnswersLabel = DateOfChange" >> ../conf/messages.en
echo "dateOfChange.error.required.all = Enter the dateOfChange" >> ../conf/messages.en
echo "dateOfChange.error.required.two = The dateOfChange" must include {0} and {1} >> ../conf/messages.en
echo "dateOfChange.error.required = The dateOfChange must include {0}" >> ../conf/messages.en
echo "dateOfChange.error.invalid = Enter a real DateOfChange" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDateOfChangeUserAnswersEntry: Arbitrary[(DateOfChangePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DateOfChangePage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDateOfChangePage: Arbitrary[DateOfChangePage.type] =";\
    print "    Arbitrary(DateOfChangePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DateOfChangePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "";\
     print "  def dateOfChange: Option[AnswerRow] = userAnswers.get(DateOfChangePage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"dateOfChange.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x.format(dateFormatter)),";\
     print "        routes.DateOfChangeController.onPageLoad(CheckMode).url";\
     print "      )";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration DateOfChange completed"
