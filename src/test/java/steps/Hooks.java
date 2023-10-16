package steps;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.CommonMethods;
import utils.Driver;

import java.util.concurrent.TimeUnit;

public class Hooks {

    @Before
    public void start() {
        Driver.getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Driver.getDriver().manage().window().maximize();
        //Driver.getDriver().get(ConfigurationReader.getProperty("url"));
    }

    @After
    public void end(Scenario scenario) {

        byte[] picture;
        if (scenario.isFailed()) {
            // take screenshot and save it in /failed
            picture = CommonMethods.takeScreenshot("failed/" + scenario.getName());
        } else {
            // take screenshot and put it in /passed folder
            picture = CommonMethods.takeScreenshot("passed/" );
        }

        scenario.attach(picture, "image/png", scenario.getName());

        Driver.closeDriver();
    }

}
