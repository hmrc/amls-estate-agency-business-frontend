#!/bin/bash

echo ""
echo "Applying migration PenalisedProfessionalBody"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /penalisedProfessionalBody                        controllers.PenalisedProfessionalBodyController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /penalisedProfessionalBody                        controllers.PenalisedProfessionalBodyController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePenalisedProfessionalBody                  controllers.PenalisedProfessionalBodyController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePenalisedProfessionalBody                  controllers.PenalisedProfessionalBodyController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "penalisedProfessionalBody.title = penalisedProfessionalBody" >> ../conf/messages.en
echo "penalisedProfessionalBody.heading = penalisedProfessionalBody" >> ../conf/messages.en
echo "penalisedProfessionalBody.checkYourAnswersLabel = penalisedProfessionalBody" >> ../conf/messages.en
echo "penalisedProfessionalBody.error.required = Select yes if penalisedProfessionalBody" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedProfessionalBodyUserAnswersEntry: Arbitrary[(PenalisedProfessionalBodyPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PenalisedProfessionalBodyPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedProfessionalBodyPage: Arbitrary[PenalisedProfessionalBodyPage.type] =";\
    print "    Arbitrary(PenalisedProfessionalBodyPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PenalisedProfessionalBodyPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def penalisedProfessionalBody: Option[AnswerRow] = userAnswers.get(PenalisedProfessionalBodyPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"penalisedProfessionalBody.checkYourAnswersLabel\")),";\
     print "        yesOrNo(x),";\
     print "        routes.PenalisedProfessionalBodyController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PenalisedProfessionalBody completed"
