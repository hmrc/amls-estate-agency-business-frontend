#!/bin/bash

echo ""
echo "Applying migration EabServicesProvided"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /eabServicesProvided                        controllers.EabServicesProvidedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /eabServicesProvided                        controllers.EabServicesProvidedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeEabServicesProvided                  controllers.EabServicesProvidedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeEabServicesProvided                  controllers.EabServicesProvidedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "eabServicesProvided.title = Which services does your business provide?" >> ../conf/messages.en
echo "eabServicesProvided.heading = Which services does your business provide?" >> ../conf/messages.en
echo "eabServicesProvided.assetManagement = Asset management" >> ../conf/messages.en
echo "eabServicesProvided.auctioneering = Auctioneering" >> ../conf/messages.en
echo "eabServicesProvided.checkYourAnswersLabel = Which services does your business provide?" >> ../conf/messages.en
echo "eabServicesProvided.error.required = Select eabServicesProvided" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEabServicesProvidedUserAnswersEntry: Arbitrary[(EabServicesProvidedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EabServicesProvidedPage.type]";\
    print "        value <- arbitrary[EabServicesProvided].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEabServicesProvidedPage: Arbitrary[EabServicesProvidedPage.type] =";\
    print "    Arbitrary(EabServicesProvidedPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEabServicesProvided: Arbitrary[EabServicesProvided] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(EabServicesProvided.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EabServicesProvidedPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def eabServicesProvided: Option[AnswerRow] = userAnswers.get(EabServicesProvidedPage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"eabServicesProvided.checkYourAnswersLabel\")),";\
     print "        Html(x.map(value => HtmlFormat.escape(messages(s\"eabServicesProvided.$value\")).toString).mkString(\",<br>\")),";\
     print "        routes.EabServicesProvidedController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration EabServicesProvided completed"
