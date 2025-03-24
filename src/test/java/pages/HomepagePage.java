package pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.CommonMethods;

import java.util.List;

public class HomepagePage extends MainPage {


    public List<String> getAllItems(){
        return CommonMethods.getAllItemsString(listOfTopMenuOptions);
    }


    @FindBy(xpath = "//div[@id=\"desktop-category-topnav\"]//li/a")
    public List<WebElement> listOfTopMenuOptions;
}
