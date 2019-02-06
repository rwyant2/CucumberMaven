package stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import pages.*;

public class CommonStepDefs {
    IndexPage indexPage = new IndexPage();
    ResultsPage resultsPage = new ResultsPage();

    @Given("^user goes to html page$")
    public void user_goes_to_html5_page() {
        switch(indexPage.getPageTitle()) {
            case "index": indexPage.goToHTML5Page(); break;
            case "results": resultsPage.goToHTML5Page(); break;
            default: Assert.fail("Cannot navigate to HTML5 page");
        }
    }

	@When("^user enters \"([^\"]*)\" in field where label contains \"([^\"]*)\"$")
	public void user_enters_in_field_where_label_contains(String text, String label) { indexPage.enterTextInField(text,label); }

	@And("^user clicks button labeled \"([^\"]*)\"$")
    public void user_clicks_button_labeled(String label) {
        indexPage.clickButton(label);
    }

    @Then("^the text with id \"([^\"]*)\" displays \"([^\"]*)\"$")
    public void the_text_with_id_displays(String id, String value) {
        indexPage.verifyTextField(id, value);
    }
}
