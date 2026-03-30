package pages;

import com.microsoft.playwright.Page;
import utils.Driver;

public class MainPage {

    protected Page page;

    public MainPage() {
        this.page = Driver.getPage();
    }
}
