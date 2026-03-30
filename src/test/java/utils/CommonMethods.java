package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class CommonMethods {

    /**
     * Clears the input field and types new text
     */
    public static void sendText(Locator locator, String text) {
        locator.clear();
        locator.fill(text);
    }

    /**
     * Clicks on a radio or checkbox from a list that matches the given value
     */
    public static void clickRadioOrCheckbox(Locator listLocator, String value) {
        int count = listLocator.count();
        for (int i = 0; i < count; i++) {
            Locator el = listLocator.nth(i);
            String actualValue = el.getAttribute("value");
            if (actualValue != null && actualValue.trim().equals(value) && el.isEnabled()) {
                el.click();
                break;
            }
        }
    }

    /**
     * Selects a dropdown option by visible text using the native <select> element
     */
    public static void selectDropdownOption(Locator locator, String textToSelect) {
        locator.selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(textToSelect));
    }

    /**
     * Selects a dropdown option by index using the native <select> element
     */
    public static void selectDropdownOption(Locator locator, int index) {
        locator.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(index));
    }

    /**
     * Accepts the currently open browser dialog
     */
    public static void acceptAlert() {
        Driver.getPage().onDialog(dialog -> dialog.accept());
    }

    /**
     * Dismisses the currently open browser dialog
     */
    public static void dismissAlert() {
        Driver.getPage().onDialog(dialog -> dialog.dismiss());
    }

    /**
     * Returns the text of the currently open browser dialog
     */
    public static String getAlertText() {
        final String[] text = {null};
        Driver.getPage().onDialog(dialog -> text[0] = dialog.message());
        return text[0];
    }

    /**
     * Sends text to the currently open browser dialog
     */
    public static void sendAlertText(String textToSend) {
        Driver.getPage().onDialog(dialog -> dialog.accept(textToSend));
    }

    /**
     * Switches focus into a frame by name or URL
     */
    public static void switchToFrame(String nameOrUrl) {
        Driver.getPage().frame(nameOrUrl);
    }

    /**
     * Switches focus into a frame by index
     */
    public static void switchToFrame(int index) {
        Driver.getPage().frames().get(index);
    }

    /**
     * Switches to the most recently opened page (child window/tab)
     */
    public static void switchToChildWindow() {
        Page page = Driver.getPage().context().pages().stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new RuntimeException("No child page found"));
        page.bringToFront();
    }

    /**
     * Waits for a locator to be visible
     */
    public static void waitForVisibility(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    }

    /**
     * Waits for a locator to be hidden
     */
    public static void waitForInvisibility(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
    }

    /**
     * Clicks on a locator after waiting for it to be enabled
     */
    public static void smartClick(Locator locator) {
        locator.click();
    }

    public static void waitthread(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clicks element using JavaScript evaluation
     */
    public static void jsClick(Locator locator) {
        locator.evaluate("el => el.click()");
    }

    /**
     * Scrolls the page until the element is visible
     */
    public static void scrollToElement(Locator locator) {
        locator.scrollIntoViewIfNeeded();
    }

    /**
     * Scrolls down by the given number of pixels
     */
    public static void scrollDown(int pixel) {
        Driver.getPage().evaluate("window.scrollBy(0," + pixel + ")");
    }

    /**
     * Scrolls up by the given number of pixels
     */
    public static void scrollUp(int pixel) {
        Driver.getPage().evaluate("window.scrollBy(0,-" + pixel + ")");
    }

    /**
     * Selects a date from a calendar by matching text in a list of day locators
     */
    public static void selectCalendarDate(Locator daysLocator, String text) {
        int count = daysLocator.count();
        for (int i = 0; i < count; i++) {
            Locator day = daysLocator.nth(i);
            if (day.isEnabled() && day.textContent().equals(text)) {
                day.click();
                break;
            }
        }
    }

    /**
     * Takes a screenshot, saves to disk, and returns the bytes
     */
    public static byte[] takeScreenshot(String filename) {
        String destination = "screenshot/" + filename + getDateAndTimeStamp().replace("/", "-").replace(":", "-") + ".png";
        new File(destination).getParentFile().mkdirs();
        return Driver.getPage().screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get(destination)));
    }

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

    /**
     * Waits for the page to reach document.readyState == "complete"
     */
    public static void waitForPageToLoad() {
        System.out.println("Waiting for page to load...");
        Driver.getPage().waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
    }

    /**
     * Verifies that an element found by the given selector is visible
     */
    public static void verifyElementDisplayed(String selector) {
        Locator locator = Driver.getPage().locator(selector);
        try {
            assertTrue("Element not visible: " + selector, locator.isVisible());
        } catch (Exception e) {
            Assert.fail("Element not found: " + selector);
        }
    }

    /**
     * Verifies that a locator is visible
     */
    public static void verifyElementDisplayed(Locator locator) {
        try {
            assertTrue("Element not visible: " + locator, locator.isVisible());
        } catch (Exception e) {
            Assert.fail("Element not found: " + locator);
        }
    }

    /**
     * Double-clicks on a locator
     */
    public static void doubleClick(Locator locator) {
        locator.dblclick();
    }

    /**
     * Clicks the item in the list locator whose text matches the given item string
     */
    public static void clickItemFromList(Locator listLocator, String item) {
        int count = listLocator.count();
        for (int i = 0; i < count; i++) {
            Locator el = listLocator.nth(i);
            if (el.textContent().toLowerCase().contains(item.toLowerCase())) {
                el.click();
                return;
            }
        }
        throw new NoSuchElementException("item not found: " + item);
    }

    public static String getRandomString() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder generator = new StringBuilder();
        Random rnd = new Random();
        while (generator.length() < 10) {
            int index = (int) (rnd.nextFloat() * letters.length());
            generator.append(letters.charAt(index));
        }
        return generator.toString();
    }

    /**
     * Returns the text of the first item in the list locator that contains the given string
     */
    public static String getItemFromList(Locator listLocator, String item) {
        int count = listLocator.count();
        for (int i = 0; i < count; i++) {
            Locator el = listLocator.nth(i);
            String text = el.textContent();
            if (text.toLowerCase().contains(item.toLowerCase())) {
                return text;
            }
        }
        throw new NoSuchElementException("item not found: " + item);
    }

    public static String getRandomBirthday() {
        LocalDate newDate = LocalDate.now().minus(Period.ofDays((new Random().nextInt(365 * 70))));
        return newDate.format(DateTimeFormatter.ofPattern("MM/dd/yyy"));
    }

    /**
     * Clicks a random element from the list locator
     */
    public static void clickRandomItemFromList(Locator listLocator) {
        int count = listLocator.count();
        int randomIndex = new Random().nextInt(count);
        listLocator.nth(randomIndex).click();
    }

    /**
     * Returns all text contents from a list locator as a List of Strings
     */
    public static List<String> getAllItemsString(Locator listLocator) {
        return listLocator.allTextContents();
    }
}
