package steps;

import io.cucumber.java.en.When;
import pages.HomepagePage;

public class HomePageSteps {

    HomepagePage homepagePage=new HomepagePage();
    @When("the user clicks Holiday shop button")
    public void theUserClicksHolidayShopButton() {

    }

    @When("the user searches the book in the search box")
    public void theUserSearchesTheBookInTheSearchBox() {
        System.out.println("test");
        System.out.println(homepagePage.getAllItems());

    }
}
