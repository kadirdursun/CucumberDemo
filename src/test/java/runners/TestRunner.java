package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty", //prints gherkin steps in console
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "html:target-output", //create a basic html report in target folder
                "json:target/cucumber.json"


        }
)
public class TestRunner {

}
