#!/bin/bash

echo ""
echo "Applying migration PenalisedEstateAgentsAct"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /penalisedEstateAgentsAct                        controllers.PenalisedEstateAgentsActController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /penalisedEstateAgentsAct                        controllers.PenalisedEstateAgentsActController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePenalisedEstateAgentsAct                  controllers.PenalisedEstateAgentsActController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePenalisedEstateAgentsAct                  controllers.PenalisedEstateAgentsActController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "penalisedEstateAgentsAct.title = penalisedEstateAgentsAct" >> ../conf/messages.en
echo "penalisedEstateAgentsAct.heading = penalisedEstateAgentsAct" >> ../conf/messages.en
echo "penalisedEstateAgentsAct.checkYourAnswersLabel = penalisedEstateAgentsAct" >> ../conf/messages.en
echo "penalisedEstateAgentsAct.error.required = Select yes if penalisedEstateAgentsAct" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedEstateAgentsActUserAnswersEntry: Arbitrary[(PenalisedEstateAgentsActPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PenalisedEstateAgentsActPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedEstateAgentsActPage: Arbitrary[PenalisedEstateAgentsActPage.type] =";\
    print "    Arbitrary(PenalisedEstateAgentsActPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PenalisedEstateAgentsActPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def penalisedEstateAgentsAct: Option[AnswerRow] = userAnswers.get(PenalisedEstateAgentsActPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"penalisedEstateAgentsAct.checkYourAnswersLabel\")),";\
     print "        yesOrNo(x),";\
     print "        routes.PenalisedEstateAgentsActController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PenalisedEstateAgentsAct completed"
