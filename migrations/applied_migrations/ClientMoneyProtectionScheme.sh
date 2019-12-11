#!/bin/bash

echo ""
echo "Applying migration ClientMoneyProtectionScheme"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /clientMoneyProtectionScheme                        controllers.ClientMoneyProtectionSchemeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /clientMoneyProtectionScheme                        controllers.ClientMoneyProtectionSchemeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeClientMoneyProtectionScheme                  controllers.ClientMoneyProtectionSchemeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeClientMoneyProtectionScheme                  controllers.ClientMoneyProtectionSchemeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "clientMoneyProtectionScheme.title = clientMoneyProtectionScheme" >> ../conf/messages.en
echo "clientMoneyProtectionScheme.heading = clientMoneyProtectionScheme" >> ../conf/messages.en
echo "clientMoneyProtectionScheme.checkYourAnswersLabel = clientMoneyProtectionScheme" >> ../conf/messages.en
echo "clientMoneyProtectionScheme.error.required = Select yes if clientMoneyProtectionScheme" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryClientMoneyProtectionSchemeUserAnswersEntry: Arbitrary[(ClientMoneyProtectionSchemePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ClientMoneyProtectionSchemePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryClientMoneyProtectionSchemePage: Arbitrary[ClientMoneyProtectionSchemePage.type] =";\
    print "    Arbitrary(ClientMoneyProtectionSchemePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ClientMoneyProtectionSchemePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def clientMoneyProtectionScheme: Option[AnswerRow] = userAnswers.get(ClientMoneyProtectionSchemePage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"clientMoneyProtectionScheme.checkYourAnswersLabel\")),";\
     print "        yesOrNo(x),";\
     print "        routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ClientMoneyProtectionScheme completed"
