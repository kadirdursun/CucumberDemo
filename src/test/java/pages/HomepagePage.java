package pages;

import com.microsoft.playwright.Locator;
import utils.CommonMethods;

import java.util.List;

public class HomepagePage extends MainPage {

    private final Locator listOfTopMenuOptions =
            page.locator("//div[@id='desktop-category-topnav']//li/a");

    public List<String> getAllItems() {
        return CommonMethods.getAllItemsString(listOfTopMenuOptions);
    }
}
