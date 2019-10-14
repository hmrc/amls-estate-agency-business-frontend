#!/bin/bash

echo ""
echo "Applying migration PenalisedProfessionalBodyDetail"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /penalisedProfessionalBodyDetail                        controllers.PenalisedProfessionalBodyDetailController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /penalisedProfessionalBodyDetail                        controllers.PenalisedProfessionalBodyDetailController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePenalisedProfessionalBodyDetail                  controllers.PenalisedProfessionalBodyDetailController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePenalisedProfessionalBodyDetail                  controllers.PenalisedProfessionalBodyDetailController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "penalisedProfessionalBodyDetail.title = penalisedProfessionalBodyDetail" >> ../conf/messages.en
echo "penalisedProfessionalBodyDetail.heading = penalisedProfessionalBodyDetail" >> ../conf/messages.en
echo "penalisedProfessionalBodyDetail.checkYourAnswersLabel = penalisedProfessionalBodyDetail" >> ../conf/messages.en
echo "penalisedProfessionalBodyDetail.error.required = Enter penalisedProfessionalBodyDetail" >> ../conf/messages.en
echo "penalisedProfessionalBodyDetail.error.length = PenalisedProfessionalBodyDetail must be 255 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedProfessionalBodyDetailUserAnswersEntry: Arbitrary[(PenalisedProfessionalBodyDetailPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PenalisedProfessionalBodyDetailPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPenalisedProfessionalBodyDetailPage: Arbitrary[PenalisedProfessionalBodyDetailPage.type] =";\
    print "    Arbitrary(PenalisedProfessionalBodyDetailPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PenalisedProfessionalBodyDetailPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def penalisedProfessionalBodyDetail: Option[AnswerRow] = userAnswers.get(PenalisedProfessionalBodyDetailPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"penalisedProfessionalBodyDetail.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x),";\
     print "        routes.PenalisedProfessionalBodyDetailController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PenalisedProfessionalBodyDetail completed"
