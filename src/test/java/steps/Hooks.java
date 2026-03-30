package steps;

import com.microsoft.playwright.Page;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigurationReader;
import utils.Driver;

import java.nio.file.Paths;

public class Hooks {

    @Before
    public void start() {
        Page page = Driver.getPage();
        page.setViewportSize(1920, 1080);
        page.navigate(ConfigurationReader.getProperty("url"));
    }

    @After
    public void end(Scenario scenario) {
        Page page = Driver.getPage();
        String folder = scenario.isFailed() ? "failed/" : "passed/";
        String safeName = scenario.getName().replaceAll("[^a-zA-Z0-9_-]", "_");

        new java.io.File("screenshot/" + folder).mkdirs();
        byte[] picture = page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot/" + folder + safeName + ".png")));

        scenario.attach(picture, "image/png", scenario.getName());

        Driver.closeDriver();
    }
}
