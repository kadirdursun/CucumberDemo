$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/features/LoginPage.feature");
formatter.feature({
  "name": "Sysdigcloud_LoginPage",
  "description": "  As a user I should be able to see login logo and forgot password button in the login page",
  "keyword": "Feature",
  "tags": [
    {
      "name": "@loginPage"
    }
  ]
});
formatter.scenario({
  "name": "User is able to verify login logo and forgot password button in the login page",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@loginPage"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "the user gets the login page, verifies the login logo",
  "keyword": "When "
});
formatter.match({
  "location": "stepdefinitions.LoginSteps.theUserGetsTheLoginPageVerifiesTheLoginLogo()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "the user verifies forgot password button in the login page",
  "keyword": "And "
});
formatter.match({
  "location": "stepdefinitions.LoginSteps.theUserVerifiesForgotPasswordButtonInTheLoginPage()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});