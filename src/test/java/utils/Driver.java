package utils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.net.URL;

public class Driver {

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();
    private Driver(){ }
    public static WebDriver getDriver(){

        if(driverPool.get() == null) {
            synchronized (Driver.class) {
                String browser = ConfigurationReader.getProperty("browser");
                if (System.getProperty("browser") != null) {
                    System.out.println("Browser type was changed to: " + System.getProperty("browser"));
                    browser = System.getProperty("browser");
                }
                switch (browser) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver(chromeOptions));
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver());
                        break;
                    case "ie":
                        WebDriverManager.iedriver().setup();
                        driverPool.set(new InternetExplorerDriver());
                        break;
                    case "safari":
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        driverPool.set(new  SafariDriver());
                        break;
                    case "headless-chrome":
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                        break;
                    case "remote-chrome":
                        try {
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("chrome");
                            String ip = System.getProperty("webdriverIP") != null
                                    ? System.getProperty("webdriverIP")
                                    : "localhost";
                            URL gridUrl = new URL("http", ip, 4444, "/wd/hub");
                            ChromeOptions chromeOptions2 = new ChromeOptions();
                            chromeOptions2.addArguments("--disable-dev-shm-usage");
                            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,chromeOptions2);
                            driverPool.set(new RemoteWebDriver(gridUrl, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "remote-firefox":
                        try {
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("firefox");
                            URL gridUrl = new URL("http://www.ec2machine:4444/wd/hub");
                            driverPool.set(new RemoteWebDriver(gridUrl, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "firefox-headless":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                        break;
                    default:
                        throw new RuntimeException("No such a browser yet!");
                }
            }
        }
        return driverPool.get();
    }

    public static void closeDriver(){
        if (driverPool.get() != null){
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}