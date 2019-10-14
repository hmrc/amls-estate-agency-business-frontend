#!/bin/bash

echo ""
echo "Applying migration PenalisedEstateAgentsActDetail"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /penalisedEstateAgentsActDetail                        controllers.PenalisedEstateAgentsActDetailController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /penalisedEstateAgentsActDetail                        controllers.PenalisedEstateAgentsActDetailController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePenalisedEstateAgentsActDetail                  controllers.PenalisedEstateAgentsActDetailController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePenalisedEstateAgentsActDetail                  controllers.PenalisedEstateAgentsActDetailController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "penalisedEstateAgentsActDetail.title = penalisedEstateAgentsActDetail" >> ../conf/messages.en
echo "penalisedEstateAgentsActDetail.heading = penalisedEstateAgentsActDetail" >> ../conf/messages.en
echo "penalisedEstateAgentsActDetail.checkYourAnswersLabel = penalisedEstateAgentsActDetail" >> ../conf/messages.en
echo "penalisedEstateAgentsActDetail.error.required = Enter penalisedEstateAgentsActDetail" >> ../conf/messages.en
echo "penalisedEstateAgentsActDetail.error.length = PenalisedEstateAgentsActDetail must be 255 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedEstateAgentsActDetailUserAnswersEntry: Arbitrary[(PenalisedEstateAgentsActDetailPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PenalisedEstateAgentsActDetailPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedEstateAgentsActDetailPage: Arbitrary[PenalisedEstateAgentsActDetailPage.type] =";\
    print "    Arbitrary(PenalisedEstateAgentsActDetailPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PenalisedEstateAgentsActDetailPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def penalisedEstateAgentsActDetail: Option[AnswerRow] = userAnswers.get(PenalisedEstateAgentsActDetailPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"penalisedEstateAgentsActDetail.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x),";\
     print "        routes.PenalisedEstateAgentsActDetailController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PenalisedEstateAgentsActDetail completed"
