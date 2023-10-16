package utils;



import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.NoSuchElementException;

import com.google.common.base.Function;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import static org.junit.Assert.assertTrue;

public class CommonMethods {

    public static Boolean waitForInvisibility(WebElement element) {
        return getWaitObject().until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * This method clears the textbox and sends another text
     *
     * @param element
     * @param text
     */
    public static void sendText(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * This method checks if radio/checkbox is enabled and then clicks on the
     * element that has the value we want
     *
     * @param listElement
     * @param value
     */
    public static void clickRadioOrCheckbox(List<WebElement> listElement, String value) {
        String actualValue;

        for (WebElement el : listElement) {
            actualValue = el.getAttribute("value").trim();
            if (el.isEnabled() && actualValue.equals(value)) {
                el.click();
                break;
            }
        }
    }

    /**
     * This method checks if the text is found in the dropdown element and only then
     * it selects it
     *
     * @param element
     * @param textToSelect
     */
    public static void selectDropdownOption(WebElement element, String textToSelect) {
        try {
            Select select = new Select(element);

            List<WebElement> options = select.getOptions();

            for (WebElement el : options) {
                if (el.getText().equals(textToSelect)) {
                    select.selectByVisibleText(textToSelect);
                    break;
                }
            }
        } catch (UnexpectedTagNameException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method checks if the index is valid and only then selects it
     *
     * @param element
     * @param index
     */
    public static void selectDropdownOption(WebElement element, int index) {

        try {
            Select select = new Select(element);

            int size = select.getOptions().size();

            if (size > index) {
                select.selectByIndex(index);
            }
        } catch (UnexpectedTagNameException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method accepts alerts and catches exception if alert in not present
     */
    public static void acceptAlert() {
        try {
            Alert alert = Driver.getDriver().switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method will dismiss the alert after checking if alert is present
     */
    public static void dismissAlert() {
        try {
            Alert alert = Driver.getDriver().switchTo().alert();
            alert.dismiss();
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns the alert text. If no alert is present exception is
     * caught and null is returned.
     *
     * @return
     */
    public static String getAlertText() {
        String alertText = null;

        try {
            Alert alert = Driver.getDriver().switchTo().alert();
            alertText = alert.getText();
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }

        return alertText;
    }

    /**
     * This method send text to the alert. NoAlertPresentException is handled.
     *
     * @param text
     */
    public static void sendAlertText(String text) {
        try {
            Alert alert = Driver.getDriver().switchTo().alert();
            alert.sendKeys(text);
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method switches to a frame by using name or id
     *
     * @param nameOrId
     */
    public static void switchToFrame(String nameOrId) {
        try {
            Driver.getDriver().switchTo().frame(nameOrId);
        } catch (NoSuchFrameException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method switches to a frame by using an index
     *
     * @param index
     */
    public static void switchToFrame(int index) {
        try {
            Driver.getDriver().switchTo().frame(index);
        } catch (NoSuchFrameException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method switches to a frame by using a WebElement
     *
     * @param element
     */
    public static void switchToFrame(WebElement element) {
        try {
            Driver.getDriver().switchTo().frame(element);
        } catch (NoSuchFrameException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method switches focus to a child window
     */
    public static void switchToChildWindow() {
        String mainWindow = Driver.getDriver().getWindowHandle();
        Set<String> windows = Driver.getDriver().getWindowHandles();

        for (String window : windows) {
            if (!window.equals(mainWindow)) {
                Driver.getDriver().switchTo().window(window);
            }
        }

    }

    /**
     * This method creates a WebDriverWait object and returns it
     *
     * @return
     */
    public static WebDriverWait getWaitObject() {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        // WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 15);

        return wait;
    }

    /**
     * This method waits for an item to be clickable
     *
     * @param element
     * @return
     */
    public static WebElement waitForClickability(WebElement element) {
        return getWaitObject().until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * This method waits for an element to be visible
     *
     * @param element
     * @return
     */
    public static WebElement waitForVisibility(WebElement element) {
        return getWaitObject().until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * This method click in an element and has wait implemented on it
     *
     * @param element
     */
    public static void smartClick(WebElement element) {
        waitForClickability(element);
        element.click();
    }

    public static void waitthread(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * This methods casts the driver to a JavascriptExecutor and returns it
     *
     * @return
     */
    public static JavascriptExecutor getJSObject() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();

        return js;
    }

    /**
     * This method will click in the element passed to it using JavascriptExecutor
     *
     * @param element
     */
    public static void jsClick(WebElement element) {
        getJSObject().executeScript("arguments[0].click()", element);
    }

    /**
     * This method will scroll the page until the element passed to it becomes
     * visible
     *
     * @param element
     */
    public static void scrollToElement(WebElement element) {
        getJSObject().executeScript("arguments[0].scrollIntoView(true)", element);
    }

    /**
     * This method will scroll the page down based on the passed pixel parameter
     *
     * @param pixel
     */
    public static void scrollDown(int pixel) {
        getJSObject().executeScript("window.scrollBy(0," + pixel + ")");
    }

    /**
     * This method will scroll the page up based on the passed pixel parameter
     *
     * @param pixel
     */
    public static void scrollUp(int pixel) {
        getJSObject().executeScript("window.scrollBy(0,-" + pixel + ")");
    }

    /**
     * This method will select a date from the calendar
     *
     * @param elements
     * @param text
     */
    public static void selectCalendarDate(List<WebElement> elements, String text) {
        for (WebElement day : elements) {
            if (day.isEnabled()) {
                if (day.getText().equals(text)) {
                    day.click();
                    break;
                }
            }
        }
    }

    public static byte[] takeScreenshot(String filename) {
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();

        byte[] picBytes = ts.getScreenshotAs(OutputType.BYTES);

        File file = ts.getScreenshotAs(OutputType.FILE);
        // create destination as : filepath + filename + timestamp + .png
        String destination = "screenshot/" + filename + getDateAndTimeStamp() + ".png";

        try {
            FileUtils.copyFile(file, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return picBytes;
    }

    /**
     * Method to return the current time stamp in a String
     *
     * @return
     */
    public static String getDateAndTimeStamp() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        return sdf.format(date.getTime());

    }

    public static String getDateStamp() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(date.getTime());

    }

    public static boolean isLegalDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        return sdf.parse(s, new ParsePosition(0)) != null;
    }

    public void waitUntilClick(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        //WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 15);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForPageToLoad() {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            //WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 15);
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + 15 + " seconds");
        }
    }

    public static void waitForPageToLoading(WebElement iconLoading) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
            //WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 15);
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + 15 + " seconds");
        }
    }

    public static WebElement fluentWait(final WebElement webElement, int timeinsec) {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(timeinsec))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return webElement;
            }
        });
        return element;
    }

    public static void verifyElementDisplayed(By by) {
        try {
            assertTrue("Element not visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + by);

        }
    }

    public static void verifyElementDisplayed(WebElement element) {
        try {
            assertTrue("Element not visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + element);

        }
    }

    public static void waitForStaleElement(WebElement element) {
        int y = 0;
        while (y <= 15) {
            if (y == 1)
                try {
                    element.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public WebElement selectRandomTextFromDropdown(Select select) {
        Random random = new Random();
        List<WebElement> weblist = select.getOptions();
        int optionIndex = 1 + random.nextInt(weblist.size() - 1);
        select.selectByIndex(optionIndex);
        return select.getFirstSelectedOption();
    }

    public void doubleClick(WebElement element) {
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    public static String getDropdownListOptionsText(WebElement element, String textToSelect) {
        Select select = new Select(element);
        List<WebElement> options = select.getOptions();
        for (WebElement el : options) {
            if (el.getText().equals(textToSelect)) {
                select.selectByVisibleText(textToSelect);
                break;
            }
        }
        return select.getFirstSelectedOption().getText();
    }

    public static void clickItemFromList(List<WebElement> listElement, String item) {
        listElement.stream()
                .filter(ele -> ele.getText().toLowerCase().contains(item.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("item not found: " + item))
                .click();
    }

    public static String getRandomString() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder generator = new StringBuilder();
        Random rnd = new Random();
        while (generator.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * letters.length());
            generator.append(letters.charAt(index));
        }
        String newString = generator.toString();
        return newString;
    }

    public static String getItemFromList(List<WebElement> listElement, String item) {
        return listElement.stream()
                .filter(ele -> ele.getText().toLowerCase().contains(item.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("item not found: " + item))
                .getText();

    }

    public static String getRandomBirthday() {
        LocalDate newDate = LocalDate.now().minus(Period.ofDays((new Random().nextInt(365 * 70))));
        return newDate.format(DateTimeFormatter.ofPattern("MM/dd/yyy"));
    }

//    public static String getFirstDateOfCurrentMonth() {
//        var dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
//        return dateTimeFormatter.format(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
//    }

    public static void clickRandomItemFromList(List<WebElement> elements) {
        int maxProducts = elements.size();
        // get random number
        Random random = new Random();
        int randomProduct = random.nextInt(maxProducts);
        // Select the list item
        elements.get(randomProduct).click();
    }

    public static List<String> getAllItemsString(List<WebElement> elements){
        ArrayList<String> items=new ArrayList<String>();
        for (int i=0; i< elements.size(); i++){
            items.add(elements.get(i).getText());
        }
        return items;
    }
}